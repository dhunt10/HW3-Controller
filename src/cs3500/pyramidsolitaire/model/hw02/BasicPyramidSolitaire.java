package cs3500.pyramidsolitaire.model.hw02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * BasicPyramidSolitaire is the entire model for the game of pyramid solitaire.
 * It consists of a start game method along with other in order to manipulate the game enough
 * so the player can win.
 * RULES
 * The game will be initializied with a pyramid of rows n.
 * To win you must remove all the cards in the pyramid.
 * To remove a card you have four options.
 * You can remove cards in the pyramid if and only if they are exposed, meaning
 * there are no cards covering them from below.
 * You may remove a single exposed card whose value is 13 with the 'rm1' command. Followed by
 * row card
 * You may remove two exposed cards whose values add up to 13 with the 'rm2' command. Followed by
 * row1 card1 row2 card2
 * You may remove an exposed card if you and a card in the draw pile if their sums add to 13. This
 * is done by using the 'rmwd' command. Followed by index row1 card1
 * Finally you may discard cards in the discard pile for new ones with the 'dd' command. Followed
 * by index1
 * Good luck!
 *
 */
public class BasicPyramidSolitaire implements PyramidSolitaireModel<Card> {

  private boolean gameStart;
  private int rows;
  private int numDraw;
  private Card[][] currentGameDeck;
  private List<Card> currentSideDeck;
  private boolean gameWon = false;

  /**
   * constructor that waits for start game to be called.
   */
  public BasicPyramidSolitaire() {
    gameStart = false;
  }

  @Override
  public void startGame(List<Card> deck, boolean shouldShuffle, int numRows, int numDraw) {

    if (deck == null) {
      throw new IllegalStateException("null deck");
    }

    if ((deck.size() != 52)) {
      throw new IllegalArgumentException("wrong number of cards in deck");
    }

    for (int i = 0; i < 52; i++) {
      if (deck.get(i).value > 13 || deck.get(i).value < 1) {
        throw new IllegalArgumentException("invalid value in deck");
      }

      if ((deck.get(i).suit != '♣' && deck.get(i).suit != '♦'
          && deck.get(i).suit != '♥' && deck.get(i).suit != '♠')) {
        throw new IllegalArgumentException("invalid suit in deck");
      }

    }

    if (numDraw < 0 ) {
      throw new IllegalArgumentException("Num draw error");
    }

    if (numRows < 1 || numRows > 9) {
      throw new IllegalArgumentException("exceeds limit");
    }

    this.numDraw = numDraw;
    this.rows = numRows;
    this.currentGameDeck = new Card[numRows + 1][numRows + 1];
    this.currentSideDeck = new ArrayList<>();

    Card currElement;
    Card checkElement;
    for (int i = 0; i < deck.size(); i++) {
      currElement = deck.get(i);
      if (currElement == null) {
        throw new IllegalArgumentException("There is a null element in your deck");
      }
      for (int j = 0; j < deck.size(); j++) {
        checkElement = deck.get(j);
        if (checkElement.equals(currElement) && i != j) {
          throw new IllegalArgumentException("Duplicates");
        }
      }
    }

    if (shouldShuffle) {
      Collections.shuffle(deck);
      if (deck.size() != 52) {
        throw new IllegalStateException("Deck is not 52");
      }
    }

    Card card;
    int counter = 0;
    for (int i = 0; i < numRows + 1; i++) {
      for (int j = 0; j < i; j++) {
        card = deck.get(counter);
        counter++;
        this.currentGameDeck[i - 1][j] = card;
      }
    }

    if (counter + numDraw > deck.size()) {
      throw new IllegalArgumentException("numDraw is too big");
    }

    for (int i = 0; i < 52 - counter; i++) {
      currentSideDeck.add(deck.get(counter + i));
    }

    this.gameStart = true;
  }

  @Override
  public List<Card> getDeck() {

    List<Card> myList = new ArrayList<>();
    char suit;
    for (int i = 0; i < 4; i++) {
      if (i == 0) {
        suit = '♣';
      } else if (i == 1) {
        suit = '♦';
      } else if (i == 2) {
        suit = '♥';
      } else {
        suit = '♠';
      }
      for (int j = 1; j < 14; j++) {
        Card myCard = new Card(j, suit);
        myList.add(myCard);
      }
    }

    return myList;
  }

  @Override
  public int getNumRows() {
    if (!this.gameStart) {
      return -1;
    }
    return this.rows;
  }

  @Override
  public int getNumDraw() {
    if (!this.gameStart) {
      return -1;
    }
    return this.numDraw;
  }

  @Override
  public int getRowWidth(int row) {
    if (!this.gameStart) {
      throw new IllegalStateException("Game has not started yet");
    }

    if (row < 0 || row > this.rows) {
      throw new IllegalArgumentException("negative row");
    }

    return row + 1;
  }

  @Override
  public boolean isGameOver() throws IllegalStateException {
    if (!this.gameStart) {
      throw new IllegalStateException("Game hasnt started yet");
    }

    List<Card> exposedCards = new ArrayList<>();
    exposedCards = getExposedCards();

    if (getScore() == 0) {
      this.gameWon = true;
    }

    for (int i = 0; i < exposedCards.size(); i++) {
      if (exposedCards.get(i).value == 13) {
        return false;
      }
    }

    if (currentSideDeck.size() > 0 && getScore() != 0) {
      return false;
    }

    for (int i = 0; i < exposedCards.size(); i++) {
      for (int j = 0; j < exposedCards.size(); j++) {
        if (exposedCards.get(i).value + exposedCards.get(j).value == 13) {
          return false;
        }
      }
    }

    for (int i = 0; i < exposedCards.size(); i++) {
      for (int k = 0; k < currentSideDeck.size(); k++) {
        if (exposedCards.get(i).value + currentSideDeck.get(k).value == 13) {
          return false;
        }
      }
    }
    //this.gameStart = false;

    return true;
  }

  @Override
  public int getScore() throws IllegalStateException {
    if (!this.gameStart) {
      throw new IllegalStateException("Game hasnt started yet");
    }

    int score = 0;
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.rows; j++) {
        if (this.currentGameDeck[i][j] != null) {
          score = score + currentGameDeck[i][j].value;
        }
      }
    }

    return score;
  }

  @Override
  public Card getCardAt(int row, int card) throws IllegalStateException {
    if (!this.gameStart) {
      throw new IllegalStateException();
    }
    if (row < 0) {
      throw new IllegalArgumentException("row is < 0");
    }
    if (row >= this.rows) {
      throw new IllegalArgumentException("row is > rows");
    }
    if (card < 0) {
      throw new IllegalArgumentException("card column is less than 0");
    }
    if (card >= this.getRowWidth(row)) {
      throw new IllegalArgumentException("card column is greater than columns");
    }


    return currentGameDeck[row][card];
  }

  @Override
  public List<Card> getDrawCards() throws IllegalStateException {
    if (!this.gameStart) {
      throw new IllegalStateException("Game has not started yet");
    }

    if (currentSideDeck.size() <= getNumDraw()) {
      return currentSideDeck.subList(0, currentSideDeck.size());
    }
    else {
      return currentSideDeck.subList(0, getNumDraw());
    }
  }

  @Override
  public void remove(int row1, int card1, int row2, int card2) throws IllegalStateException {
    if (!this.gameStart) {
      throw new IllegalStateException("Game has not started yet");
    }

    if (row1 < 0 || row1 >= this.rows
        || row2 < 0 || row2 >= this.rows
        || card1 < 0 || card1 >= this.getRowWidth(row1)
        || card2 < 0 || card2 >= this.getRowWidth(row2)) {
      throw new IllegalArgumentException("invalid row/card");
    }

    if (row1 == row2 && card1 == card2) {
      throw new IllegalArgumentException("Those are the same card");
    }

    List<Card> exposedCards = new ArrayList<>();
    exposedCards = getExposedCards();

    if (exposedCards.contains(currentGameDeck[row1][card1])) {
      if (exposedCards.contains(currentGameDeck[row2][card2])) {
        if (currentGameDeck[row1][card1].value + currentGameDeck[row2][card2].value == 13) {
          currentGameDeck[row1][card1] = null;
          currentGameDeck[row2][card2] = null;
        } else {
          throw new IllegalArgumentException("invalid move, cards don't equal 13");
        }
      } else {
        throw new IllegalArgumentException("invalid move, card 2 is not valid");
      }
    }
    else {
      throw new IllegalArgumentException("invalid move, card 1 is not valid");
    }

  }

  @Override
  public void remove(int row, int card) throws IllegalStateException {
    if (!this.gameStart) {
      throw new IllegalStateException("Game has not started yet");
    }

    if (row < 0 || row >= this.rows || card < 0 || card >= this.getRowWidth(row)) {
      throw new IllegalArgumentException("illegal row or card");
    }

    List<Card> exposedCards = new ArrayList<>();
    exposedCards = getExposedCards();

    if (exposedCards.contains(currentGameDeck[row][card])) {
      if (currentGameDeck[row][card].value == 13) {
        currentGameDeck[row][card] = null;
      } else {
        throw new IllegalArgumentException("invalid move, card is not valued at 13");
      }
    } else {
      throw new IllegalArgumentException("invalid move in remove single");
    }
  }

  @Override
  public void discardDraw(int drawIndex) throws IllegalStateException {
    if (!this.gameStart) {
      throw new IllegalStateException("Game has not started yet");
    }

    if (drawIndex >= this.numDraw) {
      throw new IllegalArgumentException("not enough draw cards");
    }

    if (drawIndex < 0) {
      throw new IllegalArgumentException("negative value for drawindex in discarddraw");
    }

    if (currentSideDeck.size() > getNumDraw()) {
      Card placeHolder;
      placeHolder = (currentSideDeck.get(getNumDraw()));
      currentSideDeck.remove(getNumDraw());
      currentSideDeck.set(drawIndex, placeHolder);
    } else {
      currentSideDeck.remove(drawIndex);
    }

  }

  @Override
  public void removeUsingDraw(int drawIndex, int row, int card) throws IllegalStateException {
    if (!this.gameStart) {
      throw new IllegalStateException("Game has not started yet");
    }

    if (drawIndex >= this.numDraw) {
      throw new IllegalArgumentException("not enough draw cards");
    }

    if (drawIndex < 0) {
      throw new IllegalArgumentException("negative value for draw value in removeusingdraw");
    }


    if (row < 0 || row >= this.rows || card < 0 || card >= this.getRowWidth(row)) {
      throw new IllegalArgumentException("illegal row or card");
    }

    List<Card> exposedCards = new ArrayList<>();
    exposedCards = getExposedCards();

    if (exposedCards.contains(currentGameDeck[row][card])
        && currentSideDeck.get(drawIndex) != null) {
      if (currentSideDeck.get(drawIndex).value + currentGameDeck[row][card].value == 13) {
        if (currentSideDeck.size() > getNumDraw()) {
          Card placeHolder;
          placeHolder = (currentSideDeck.get(getNumDraw()));
          currentSideDeck.remove(getNumDraw());
          currentSideDeck.set(drawIndex, placeHolder);
        } else {
          currentSideDeck.remove(drawIndex);
        }
        currentGameDeck[row][card] = null;

      } else {
        throw new IllegalArgumentException("cards dont add up to 13");
      }
    } else {
      throw new IllegalArgumentException("not exposed card or null");
    }


  }

  /**
   * gets a list of all current exposed cards on the pyramid.
   *
   * @return a list of type Card that holds all exposed cards.
   */
  public List<Card> getExposedCards() {

    List<Card> exposedCards = new ArrayList<>();

    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.rows; j++) {
        if (currentGameDeck[j][i] != null) {
          if (currentGameDeck[j + 1][i] == null
              && currentGameDeck[j + 1][i + 1] == null) {
            exposedCards.add(currentGameDeck[j][i]);
          }
        }
      }
    }
    return exposedCards;
  }

  /**
   * Returns a string reflecting the current state of the game.
   *
   * @return string reflecting current state of game.
   */

  private String stringDeckHelper() {

    String line = "";
    int iterSize;
    String card;
    String response = "";
    if (getNumDraw() >= currentSideDeck.size()) {
      iterSize = currentSideDeck.size();
    } else {
      iterSize = getNumDraw();
    }
    for (int i = 0; i < iterSize; i++) {
      card = currentSideDeck.get(i).toString();
      if (i == getNumDraw() - 1) {
        line = line + card;
      } else {
        line = line + card + ", ";
      }
    }
    response = response + "Draw: " + line;
    return response;
  }

  private List<Card> getAloneCards() {
    List<Card> aloneCards = new ArrayList<>();


    for (int i = 0; i < getRowWidth(i); i++) {
      for (int j = 0; j < getRowWidth(i); j++) {
        boolean cardStatus = true;
        Card tester = getCardAt(i, j);
        for (int k = j; k < getRowWidth(i); k++) {
          if (getCardAt(i,k) != null) {
            cardStatus = false;
          }
        }
        if (cardStatus) {
          aloneCards.add(tester);
        }
      }
    }
    return aloneCards;
  }

  /**
   * Renders the main part of the game.
   * @return string version of game.
   */
  private String stringTester() {

    String response = "";
    String line = "";
    String card;
    boolean lastCard = true;

    for (int i = 1; i < getNumRows() + 1; i++) {
      StringBuilder bufferSpace = new StringBuilder();
      for (int k = 2 * (getNumRows() - i); k > 0; k--) {
        bufferSpace.append(" ");
      }
      line = bufferSpace + "";

      for (int j = 0; j < i; j++) {

        if (currentGameDeck[i - 1][j] != null) {
          card = this.getCardAt(i - 1, j).toString();
          if (j != getRowWidth(i) - 2) {
            if (currentGameDeck[i - 1][j].value == 10) {
              line = line + card + " ";
            } else {
              line = line + card + "  ";
            }
          } else {
            line = line + card;
          }
        } else {
          line = line + "    ";
        }
      }

      if (line.isBlank()) {
        line = "";
      } else {
        line = line + "\n";
      }
      response = response + line;

      line = "";
    }

    response = response + stringDeckHelper();
    return response;
  }



  /**
   * renders the model into a string.
   * @return the current model in string form.
   */
  public String toString() {
    try {
      isGameOver();
    }
    catch (IllegalStateException e) {
      return "";
    }

    if (gameWon) {
      return "You win!";
    }

    String response = "";

    if (gameStart && isGameOver() && getScore() != 0) {
      response = response + "Game over!" + "\n";
      response = response + stringTester();
      return (response);

    } else {
      response = response + stringTester();
    }
    return response;
  }

}