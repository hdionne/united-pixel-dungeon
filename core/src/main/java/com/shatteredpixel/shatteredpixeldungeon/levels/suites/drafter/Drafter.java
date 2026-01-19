package com.shatteredpixel.shatteredpixeldungeon.levels.suites.drafter;

import com.shatteredpixel.shatteredpixeldungeon.levels.builders.Builder;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.suites.standard.Suite;

import java.util.ArrayList;

// This class handles the building of a Suite.
// It extends builder, so it should allow placing rooms in the rectangles that it defines.
public abstract class Drafter extends Builder {

    // Define a method to draft a set of rooms when given a room with doors.
    public abstract ArrayList<Room> draft(Suite suite);

    // The basic Builder builder method which Drafters implement can be used to place particular rooms inside the suite.

}
