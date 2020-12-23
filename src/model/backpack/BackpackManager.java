package model.backpack;
import model.shapes.Shape;
import java.util.ArrayList;

public class BackpackManager<T extends  Shape> {
    private final ArrayList<Backpack<T>> backpacks_;
    private Integer currentBackpackIndex_;

    public BackpackManager(){
        this.backpacks_ = new ArrayList<>();
        this.currentBackpackIndex_ = 0;
    }

    public BackpackManager(ArrayList<Backpack<T>> backpacks, Backpack<T> currentBackpack){
        this.backpacks_ = backpacks;
        // exception?
        this.currentBackpackIndex_ = backpacks_.indexOf(currentBackpack);
    }

    public BackpackManager(ArrayList<Backpack<T>> backpacks, Integer index){
        this.backpacks_ = backpacks;
        this.currentBackpackIndex_ = index;
    }

    public Integer getNumberOfBackpacks(){
        return backpacks_.size();
    }
    public Backpack<T> getCurrentBackpack(){
        return backpacks_.get(currentBackpackIndex_);
    }

    public void addBackpack(Backpack<T> backpack){
        backpacks_.add(backpack);
        currentBackpackIndex_ = backpacks_.indexOf(backpack);
    }

    public void deleteBackpack(Backpack<T> backpack){
        if(currentBackpackIndex_.equals(backpacks_.indexOf(backpack))){
            currentBackpackIndex_ = 0;
        }
        backpacks_.remove(backpack);
    }

    public static class FindException extends RuntimeException {
        public FindException(String message) {
            super(message);
        }
    }


    public void deleteBackpack(Integer index) throws FindException{
        if(index >= backpacks_.size()){
            throw new FindException("Index is greater than number of shapes!");
        }
        if(currentBackpackIndex_.equals(index)){
            currentBackpackIndex_ = 0;
        }
        backpacks_.remove(index);
    }

    public void setCurrentBackpack(Integer index)throws FindException {
        if(index >= backpacks_.size()){
            throw new FindException("Index is greater than number of shapes!");
        }
        currentBackpackIndex_ = index;
    }

    public void setCurrentBackpack(Backpack<T> backpack) throws FindException {
        if(!backpacks_.contains(backpack)){
            throw new FindException("You just try to set backpack, that doesn't exist!");
        }
        if(currentBackpackIndex_.equals(backpacks_.indexOf(backpack))){
            return;
        }
        currentBackpackIndex_ = backpacks_.indexOf(backpack);
    }
}
