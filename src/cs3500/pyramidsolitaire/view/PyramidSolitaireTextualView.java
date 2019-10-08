package cs3500.pyramidsolitaire.view;

import cs3500.pyramidsolitaire.model.hw02.PyramidSolitaireModel;
import java.io.IOException;


/**
 * class to render text.
 */
public class PyramidSolitaireTextualView implements PyramidSolitaireView {

  private final PyramidSolitaireModel<?> model;
  public Appendable out;

  /**
   * Initializes a game.
   *
   * @param model game of solitaire
   */
  public PyramidSolitaireTextualView(PyramidSolitaireModel<?> model) {
    this.model = model;
  }

  public PyramidSolitaireTextualView(PyramidSolitaireModel<?> model, Appendable ap) {
    this.model = model;
    this.out = ap;
  }

  @Override
  public String toString() {
    return (this.model.toString());
  }

  @Override
  public void render() throws IOException {
    try {
      this.out.append(this.model.toString());
    }
    catch (IOException e) {
      throw new IllegalArgumentException("Render!");
    }
  }
}