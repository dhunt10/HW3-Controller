package cs3500.pyramidsolitaire.controller;

import cs3500.pyramidsolitaire.model.hw02.BasicPyramidSolitaire;
import cs3500.pyramidsolitaire.model.hw02.PyramidSolitaireModel;
import cs3500.pyramidsolitaire.view.PyramidSolitaireTextualView;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;


/**
 * Class is responsible for creating a controller.
 **/
public class PyramidSolitaireTextualController implements PyramidSolitaireController {

  private final Readable in;
  private final Appendable out;

  /**
   * Constructor for controller.
   * @param rd readable that will take in a move.
   * @param ap appendable that will output a game state.
   * @throws IllegalArgumentException when the input and output is null
   **/
  public PyramidSolitaireTextualController(Readable rd, Appendable ap)
      throws IllegalArgumentException {

    if (rd == null || ap == null) {
      throw new IllegalArgumentException("Null inputs");
    }

    this.in = rd;
    this.out = ap;
  }

  @Override
  public <K> void playGame(PyramidSolitaireModel<K> model, List<K> deck, boolean shuffle,
      int numRows, int numDraw) {
    if (model == null) {
      throw new IllegalArgumentException("Null model");
    } else {
      model.startGame(deck, shuffle, numRows, numDraw);
    }

    PyramidSolitaireTextualView pyramidView = new PyramidSolitaireTextualView(model, out);

    int row1;
    int row2;
    int card1;
    int card2;
    int index1;
    int savedSpot = -1;
    String command = null;
    boolean quitGame = false;
    Scanner scan = null;

    try {
      pyramidView.render();
    }
    catch (IOException e) {
      throw new IllegalStateException("Started Game");
    }

    while (!model.toString().equals("You Win!") && !model.isGameOver() && !quitGame) {

      command = null;
      String test = null;
      try {
        scan = new Scanner(this.in);
      }
      catch (Exception e) {
        renderHelper(e.toString());
      }

      if (scan.hasNext()) {
        String move = scan.nextLine();

        String[] moves = move.split(" ");

        if (Arrays.asList(moves).contains("q") || Arrays.asList(moves).contains("Q") ) {
          command = "q";
        }

        for (int i = 0; i < moves.length; i++) {
          if (command == null) {
            if (moves[i].compareTo("rm1") == 0) {
              command = moves[i];
              savedSpot = i + 1;
              i = moves.length;
            } else if (moves[i].compareTo("rm2") == 0) {
              command = moves[i];
              savedSpot = i + 1;
              i = moves.length;
            } else if (moves[i].compareTo("rmwd") == 0) {
              command = moves[i];
              savedSpot = i + 1;
              i = moves.length;
            } else if (moves[i].compareTo("dd") == 0) {
              command = moves[i];
              savedSpot = i + 1;
              i = moves.length;
            } else if (moves[i].compareTo("q") == 0) {
              command = moves[i];
              savedSpot = i + 1;
              i = moves.length;
            }
          }
        }
        if (command == null) {
          renderHelper("Try again");
          continue;
        }

        switch (command) {
          case ("rm1"):

            row1 = -1;
            card1 = -1;

            for (int i = savedSpot; i < moves.length; i++) {

              try {
                if (row1 == -1) {
                  row1 = Integer.parseInt(moves[i]);
                  continue;
                }
                if (card1 == -1) {
                  card1 = Integer.parseInt(moves[i]);
                  continue;
                }
              }
              catch (Exception e) {
                renderHelper(e.toString());
              }

            }

            if (row1 == -1 || card1 == -1) {
              renderHelper("Invalid inputs");
              continue;
            }

            try {
              model.remove(row1 - 1, card1 - 1);
            } catch (IllegalStateException e) {
              renderHelper(e.toString());
              continue;
            } catch (IllegalArgumentException e) {
              renderHelper(e.toString());
              continue;
            }

            try {
              pyramidView.render();
              try {
                model.isGameOver();
              }
              catch (IllegalStateException e) {
                continue;
              }
              renderHelper("\n" + "Score: " + model.getScore() + "\n");
            }
            catch (IOException e) {
              throw new IllegalStateException(e.toString());
            }
            break;

          case ("rm2"):

            row1 = -1;
            row2 = -1;
            card1 = -1;
            card2 = -1;

            for (int i = savedSpot; i < moves.length; i++) {
              try {
                if (row1 == -1) {
                  row1 = Integer.parseInt(moves[i]);
                  continue;
                }
                if (card1 == -1) {
                  card1 = Integer.parseInt(moves[i]);
                  continue;
                }
                if (row2 == -1) {
                  row2 = Integer.parseInt(moves[i]);
                  continue;
                }
                if (card2 == -1) {
                  card2 = Integer.parseInt(moves[i]);
                  continue;
                }
              }
              catch (Exception e) {
                renderHelper(e.toString());
              }
            }

            if (row1 == -1 || card1 == -1 || card2 == -1 || row2 == -1) {
              renderHelper("Invalid inputs");
              continue;
            }

            try {
              model.remove(row1 - 1, card1 - 1, row2 - 1, card2 - 1);
            } catch (IllegalStateException e) {
              renderHelper(e.toString());
              continue;
            } catch (IllegalArgumentException e) {
              renderHelper(e.toString());
              continue;
            }

            try {
              pyramidView.render();
              try {
                model.isGameOver();
              }
              catch (IllegalStateException e) {
                continue;
              }
              renderHelper("\n" + "Score: " + model.getScore());
            }
            catch (IOException e) {
              throw new IllegalStateException(e.toString());
            }

            break;

          case ("rmwd"):

            index1 = -1;
            row1 = -1;
            card1 = -1;

            for (int i = savedSpot; i < moves.length; i++) {
              try {
                if (index1 == -1) {
                  index1 = Integer.parseInt(moves[i]);
                  continue;
                }
                if (row1 == -1) {
                  row1 = Integer.parseInt(moves[i]);
                  continue;
                }
                if (card1 == -1) {
                  card1 = Integer.parseInt(moves[i]);
                  continue;
                }
              }
              catch (Exception e) {
                renderHelper(e.toString());
              }

            }

            if (row1 == -1 || card1 == -1 || index1 == -1) {
              renderHelper("Invalid inputs");
              continue;
            }

            try {
              model.removeUsingDraw(index1 - 1, row1 - 1, card1 - 1);
            }
            catch (IllegalStateException e) {
              renderHelper(e.toString());
              continue;
            } catch (IllegalArgumentException e) {
              renderHelper(e.toString());
              continue;
            }

            try {
              pyramidView.render();
              try {
                model.isGameOver();
              }
              catch (IllegalStateException e) {
                continue;
              }
              renderHelper("\n" + "Score: " + model.getScore());
            }
            catch (IOException e) {
              throw new IllegalStateException(e.toString());
            }

            break;

          case ("dd"):

            index1 = -1;

            for (int i = savedSpot; i < moves.length; i++) {
              try {
                if (index1 == -1) {
                  index1 = Integer.parseInt(moves[i]);
                  continue;
                }
              }
              catch (Exception e) {
                renderHelper(e.toString());
              }
            }
            if (index1 == -1) {
              renderHelper("No input given");
              continue;
            }

            try {
              model.discardDraw(index1 - 1);
            } catch (IllegalArgumentException e) {
              renderHelper(e.toString());
            } catch (IllegalStateException e) {
              renderHelper(e.toString());
            }

            try {
              pyramidView.render();
              try {
                model.isGameOver();
              }
              catch (IllegalStateException e) {
                continue;
              }
              renderHelper("\n" + "Score: " + model.getScore());
            }
            catch (IOException e) {
              throw new IllegalStateException(e.toString());
            }

            break;

          case ("q"):
          case ("Q"):


            quitGame = true;
            BasicPyramidSolitaire restart = new BasicPyramidSolitaire();
            renderHelper("\n");
            renderHelper("Game quit!" + "\n");
            renderHelper("State of game when quit:"  + "\n");

            try {
              pyramidView.render();
              renderHelper("\n" + "Score: " + model.getScore());
            }
            catch (IOException e) {
              throw new IllegalStateException(e.toString());
            }
            break;

          default:
            renderHelper("Invalid move in form, must be in form"
                + "'rm1 x y', 'rm2 w x y z', 'rmwd x y z' or 'dd x'");
        }
      }
    }
  }

  void renderHelper(String renderingStr) {
    try {
      this.out.append(renderingStr);
    }
    catch (IOException e) {
      throw new IllegalStateException();
    }
  }

}
