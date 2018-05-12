/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

/**
 *
 * @author Amizz
 */
public class Block {
    public int Type;
    public Object Value;
    public Object Tag;
    public Object Flag;
    public float Duration;
    public Point Location;
    
    public Block() {
        Type = 0;
        Value = 0;
        Tag = null;
        Flag = null;
        Duration = 1.0f;
        Location = null;
    }
    
    public String toString() {
        return "Type:" + Type + ",Value:" + Value + ",Tag:"+Tag+",Flag:" + Flag + ",Duration:" + Duration+",Location:"+Location;
    }
}
