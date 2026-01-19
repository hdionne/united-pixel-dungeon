package com.shatteredpixel.shatteredpixeldungeon.levels.suites.standard;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.suites.drafter.Drafter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;
import java.util.Iterator;

// This class has the functionality of both a Room and a Level builder. It is a room and so can be used by level builders, has doors and connections,
// etc, but it also has its own building functionality inside of it, and as such will implement many of the same methods as levels.
// Inside it will be multiple standard rooms, which will be connected and will connect to the doors.
// They will have a Drafter and a Styler. The Drafter will Draft rooms based on the doors, and the Styler will apply different styles within
// The room. Different feels and levels can influence the likelihood of using a particular drafter / styler.
// It will have different SuiteBuilders to give it different shapes, and different SuitePainters to give them different themes inside.
// TODO: Have the size of this room and the number of subrooms scale with the number of players.
public abstract class Suite extends Room {

    public ArrayList<Room> subrooms;

    protected Drafter drafter;

    public enum SizeCategory {

        NORMAL(10, 14, 2),
        LARGE(12, 16, 3),
        GIANT(16, 20, 4);

        public final int minDim, maxDim;
        public final int roomValue;

        SizeCategory(int min, int max, int val){
            minDim = min;
            maxDim = max;
            roomValue = val;
        }

    }

    public StandardRoom.SizeCategory sizeCat;
    { setSizeCat(); }

    //Note that if a room wishes to allow itself to be forced to a certain size category,
    //but would (effectively) never roll that size category, consider using Float.MIN_VALUE
    public float[] sizeCatProbs(){
        //always normal by default
        return new float[]{1, 0, 0};
    }


    public boolean setSizeCat(){
        return setSizeCat(0, StandardRoom.SizeCategory.values().length-1);
    }

    //assumes room value is always ordinal+1
    public boolean setSizeCat( int maxRoomValue ){
        return setSizeCat(0, maxRoomValue-1);
    }

    //returns false if size cannot be set
    public boolean setSizeCat( int minOrdinal, int maxOrdinal ) {
        float[] probs = sizeCatProbs();
        StandardRoom.SizeCategory[] categories = StandardRoom.SizeCategory.values();

        if (probs.length != categories.length) return false;

        for (int i = 0; i < minOrdinal; i++)                    probs[i] = 0;
        for (int i = maxOrdinal+1; i < categories.length; i++)  probs[i] = 0;

        int ordinal = Random.chances(probs);

        if (ordinal != -1){
            sizeCat = categories[ordinal];
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int minWidth() { return sizeCat.minDim; }
    public int maxWidth() { return sizeCat.maxDim; }

    @Override
    public int minHeight() { return sizeCat.minDim; }
    public int maxHeight() { return sizeCat.maxDim; }

    //larger standard rooms generally count as multiple rooms for various counting/weighting purposes
    //but there can be exceptions
    public int sizeFactor(){
        return sizeCat.roomValue;
    }

    public int mobSpawnWeight(){
        if (isEntrance()){
            return 1; //entrance rooms don't have higher mob spawns even if they're larger
        }
        return sizeFactor();
    }

    public int connectionWeight(){
        return sizeFactor() * sizeFactor();
    }

    // Suites can't be merged with other rooms. They will be big enough on their own.
    @Override
    public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
        return false;
    }


    protected static Rect findFreeSpace(Point start, ArrayList<Room> collision, int maxSize){
        Rect space = new Rect(start.x-maxSize, start.y-maxSize, start.x+maxSize, start.y+maxSize);

        //shallow copy
        ArrayList<Room> colliding = new ArrayList<>(collision);
        do{

            //remove empty rooms and any rooms we aren't currently overlapping
            Iterator<Room> it = colliding.iterator();
            while (it.hasNext()){
                Room room = it.next();
                //if not colliding
                if ( room.isEmpty()
                        || Math.max(space.left, room.left) >= Math.min(space.right, room.right)
                        || Math.max(space.top, room.top) >= Math.min(space.bottom, room.bottom) ){
                    it.remove();
                }
            }

            //iterate through all rooms we are overlapping, and find the closest one
            Room closestRoom = null;
            int closestDiff = Integer.MAX_VALUE;
            boolean inside = true;
            int curDiff = 0;
            for (Room curRoom : colliding){

                if (start.x <= curRoom.left){
                    inside = false;
                    curDiff += curRoom.left - start.x;
                } else if (start.x >= curRoom.right){
                    inside = false;
                    curDiff += start.x - curRoom.right;
                }

                if (start.y <= curRoom.top){
                    inside = false;
                    curDiff += curRoom.top - start.y;
                } else if (start.y >= curRoom.bottom){
                    inside = false;
                    curDiff += start.y - curRoom.bottom;
                }

                if (inside){
                    space.set(start.x, start.y, start.x, start.y);
                    return space;
                }

                if (curDiff < closestDiff){
                    closestDiff = curDiff;
                    closestRoom = curRoom;
                }

            }

            int wDiff, hDiff;
            if (closestRoom != null){

                wDiff = Integer.MAX_VALUE;
                if (closestRoom.left >= start.x){
                    wDiff = (space.right - closestRoom.left) * (space.height() + 1);
                } else if (closestRoom.right <= start.x){
                    wDiff = (closestRoom.right - space.left) * (space.height() + 1);
                }

                hDiff = Integer.MAX_VALUE;
                if (closestRoom.top >= start.y){
                    hDiff = (space.bottom - closestRoom.top) * (space.width() + 1);
                } else if (closestRoom.bottom <= start.y){
                    hDiff = (closestRoom.bottom - space.top) * (space.width() + 1);
                }

                //reduce by as little as possible to resolve the collision
                if (wDiff < hDiff || wDiff == hDiff && Random.Int(2) == 0){
                    if (closestRoom.left >= start.x && closestRoom.left < space.right) space.right = closestRoom.left;
                    if (closestRoom.right <= start.x && closestRoom.right > space.left) space.left = closestRoom.right;
                } else {
                    if (closestRoom.top >= start.y && closestRoom.top < space.bottom) space.bottom = closestRoom.top;
                    if (closestRoom.bottom <= start.y && closestRoom.bottom > space.top) space.top = closestRoom.bottom;
                }
                colliding.remove(closestRoom);
            } else {
                colliding.clear();
            }

            //loop until we are no longer colliding with any rooms
        } while (!colliding.isEmpty());

        return space;
    }
}
