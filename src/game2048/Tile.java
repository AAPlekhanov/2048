package game2048;

import java.awt.Color;

public class Tile {

    int value;

    public  Tile(){
        value=0;
    }

    public Tile(int i){
        value=i;
    }

    boolean isEmpty() {
        if (value == 0) return true;
        else return false;
    }

    Color getFontColor(){
        if(value<16)
        return new Color(0x776e65);
        else return new Color(0xf9f6f2);
    }

    Color getTileColor(){
        Color res = null;
        switch (value) {
            case   0: res= new Color (0xcdc1b4); break;
            case   2:res= new Color(0xeee4da);break;
            case   4:res= new Color(0xede0c8);break;
            case   8:res= new Color(0xf2b179);break;
            case   16:res= new Color(0xf59563);break;
            case   32:res= new Color(0xf67c5f);break;
            case   64:res= new Color(0xf65e3b);break;
            case   128:res= new Color(0xedcf72);break;
            case   256:res= new Color(0xedcc61);break;
            case   512:res= new Color(0xedc850);break;
            case   1024:res= new Color(0xedc53f);break;
            case   2048:res= new Color(0xedc22e);break;
            default:  res = new Color (0xff0000); break;
        }
        return res;
    }
}
