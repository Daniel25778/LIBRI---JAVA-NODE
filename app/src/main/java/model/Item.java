package model;

public class Item {

  //  TYPES:

  //  0 - Livro
  //  1 - HQ
  //  2 - MANGÁ

    private int type;
    private Object object;

    public Item(int type, Object object) {
        this.type = type;
        this.object = object;
    }

    public int getType() {
        return type;
    }

    public Object getObject() {
        return object;
    }
}
