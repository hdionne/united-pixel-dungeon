package com.shatteredpixel.shatteredpixeldungeon.levels.suites.drafter;

import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.suites.standard.Suite;

import java.util.ArrayList;

public class BasicDrafter extends Drafter {
    @Override
    public ArrayList<Room> build(ArrayList<Room> rooms) {
        return null;
    }

    // The basic method for this drafting is to take the doors, create a rectangle just inside each, then expand them until they are all
    // touching by the edges.
    // I need a way to make sure that all the rooms are connected to each other.
    @Override
    public ArrayList<Room> draft(Suite suite) {
        return null;
    }
}
