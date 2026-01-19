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

}
