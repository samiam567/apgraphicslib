----------------
README:
----------------
Please actually read this!!! It will hopefully make grading my project a lot easier :)




----------------
LOJ (Legend of Java) Game Tips:
----------------

Make sure your sound is up!
break the pots (brown cylinders in the corner) to restore your hearts
Banging your head against the wall is fine, but don't do it too much or you'll get a headache
The enemies attack in patterns - figure out this pattern and use it to your advantage!
You cannot be hit while backflipping! (SHIFT key) Use this to dodge enemy attacks


----------------
Coding notes:
----------------
Legend of Java is a game I whipped up to display the power of the engine, it is not the main part of my final project.
It is a terrible mess of code that isn't written up to nearly the same standards as the graphics library. 

Almost every method and class in APGraphicsLib has JavaDoc style annotations that explain what that class or method do. 

My online names are apun1 and samiam567. don't let that confuse you or anything


----------------
Features and related methods/classes:
----------------
Time management - Object_draw.doUpdate(); Two numbers, FPS (Frames per second) and FStep (or FCPS, the inverse of the number of times movements and collision detection are calculated every frame) are controlled by the Object_draw class. It uses these numbers to keep ingame time constant with real-world time whilst adjusting for diferent update times 
Efficiency - The whole engine is built on efficiency. It is designed so that every component is as light as possible. Because all of the Three_dimensional components are childeren of their Two_dimensional counterparts, the engine is just as good at 2D modeling and game-making as 3D. Furthermore, 2D and 3D components can appear and interact with each other onscreen even though the 2D components have no 3D capabilities and thus none of the inefficiency of 3D rendering and modeling.
Rotation - I DID NOT use Graphics2D or Graphics3D objects in the entire engine despite knowing how to use them. I figured that would be cheating and wrote my own methods for 2D and 3D rotation using the same math as the Graphics2D and Graphics3D classes. (see Physics3DPolygon.AffineRotation3D and Physics_3DPolygon.Point3D.rotate(AffineRotation rotation) along with the Physics_2DPolygon versions)
Advanced Rotation - I also wrote a completely original advanced 3D rotation method that can rotate an object about and arbitrary Vector3D ( see Physics_3DPolygon.AffineRotation3D.rotate(Vector rotation) (when Settings.advancedRotation == true) and Physics_3DPolygon.Point3D.rotate(AffineRotation rot) (when AffineRot.advancedRotation == true)  NOTE: this does not use quaternions but a different algorithm of my own design - rotate to the plane of the Vector3D, rotate in the plane of the Vector3D, rotate back to the origional plane
Orbital rotation - Physics3DPolygons and Physics_2DPolygons can be rotated about any Coordinate in space by using setPointOfRotation(Coordinate2D newPointOfRotation, boolean rotateWithOrbit) and the setOrbitalRotation(), setOrbitalAngularVelocity(), and setOrbitalAngularAcceleration() methods. These rotations are calculated in Physics_2DPolygon.Update(double frames)
Collision - Stuff can hit stuff. More of a skeleton than an actual implementation. Found in Tangible and Object_draw.checkForCollisions(). Implemented in Physics_3DTexturedPolygon, LegendOfJava.Wall, and LegendOfJava.MainCharacterHead
The Rooms in Legend of Java are a binary tree. All four walls can hold a door to any room. 


----------------
Important Methods:
----------------

All of the time control is done in Object_draw.doUpdate();
Recursion is used in the method LegendOfJavaRunner.setRoomPPSizeRecursive() to traverse the binary tree of rooms
Putting textures on 3D models is done in Physics_3DTexturedPolygon.setTexture() and its related methods


----------------
Cool Classes:
----------------
Physics_3DPolygon a 3D render of points
Physics_3DTexturedPolygon A polygon that a texture can be applied to. Also supports Perspective viewing (See paint method)
Vector3D (duh)
Timer (Used for the JumpBack ability, and to control the timing of enemy attacks in LOJ)
Vector2D and Vector3D - not only are they crucial for the operation of the engine, they have some pretty clever updating tactics that allow them to be rich with information while also lightning fast. 
