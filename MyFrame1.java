import java.awt.event.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.universe.*;  	    // for SimpleUniverse
import javax.swing.*;
import java.awt.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;
import java.util.Enumeration;
import com.sun.j3d.audioengines.headspace.*;

class MyFrame1 extends JFrame
{
   VirtualUniverse universe = new VirtualUniverse();
   BranchGroup scene;
   TransformGroup tran = new TransformGroup();
   TransformGroup tran1a = new TransformGroup();
   TransformGroup tran2a = new TransformGroup();
   Sphere sphere1;
   Sphere sphere2;
   MyAlpha alpha;
   MyPositionPathInterpolator path1;
   float[] array1, array2;
   Transform3D transform1, transform2;
   float xval;
   float yval;
   PointSound point = new PointSound(new MediaContainer("file:./SOUND14.wav"), 1.0f, 0.0f, 0.0f, 0.0f);

   class MyBehavior extends Behavior
   {
	    private WakeupCondition wakeupCondition;

	    public MyBehavior()
	    {
			wakeupCondition = new WakeupOnCollisionEntry(sphere1);//, WakeupOnCollisionEntry.USE_GEOMETRY);
		}

		public void initialize()
		{
			wakeupOn(wakeupCondition);
		}

		public void processStimulus(Enumeration criteria)
		{
			WakeupOnCollisionEntry w = (WakeupOnCollisionEntry)criteria.nextElement();

			transform1 = new Transform3D();
			transform2 = new Transform3D();
			tran1a.getTransform(transform1);
			tran2a.getTransform(transform2);

			array1 = new float[16];
			array2 = new float[16];
			transform1.get(array1);
			transform2.get(array2);


			xval = array1[3] - array2[3];
			yval = array1[7] - array2[7];

			path1.setPosition(0, new Point3f((1.1f*xval), (1.1f*yval), 0));
			path1.setPosition(1, new Point3f((10.0f*xval), (10.0f*yval), 0));

			alpha.setStartTime(System.currentTimeMillis());
			point = new PointSound(new MediaContainer("file:./SOUND14.wav"), 1.0f, 0.0f, 0.0f, 0.0f);
			point.setEnable(true);
			System.out.println("enabled:" + point.getEnable());
			   			System.out.println(point.isPlaying());
			   			System.out.println("*****" + point.isReady());
   			System.out.println("point:" + point.getPriority());
			wakeupOn(wakeupCondition);
		}
	}


   public MyFrame1() throws Exception
   {
	    Appearance app = new Appearance();
	    app.setMaterial(new Material(new Color3f(1f, 0.6f, 0.05f),	 //ambient
								    new Color3f(0.0f, 0.0f, 0.0f), 	 //emissiv
	  							    new Color3f(1f, 0.6f, 0.5f),   //diffuse
	   							    new Color3f(0.0f, 0.0f, 0.0f),   //specular
	   							    128.0f));							 //shininess

	    Appearance app2 = new Appearance();
	    app2.setMaterial(new Material(new Color3f(0.0f, 0.7f, 0.85f),	 //ambient
	   								    new Color3f(0.0f, 0.0f, 0.0f), 	 //emissiv
	   	  							    new Color3f(0.0f, 0.7f, 0.85f),   //diffuse
	   	   							    new Color3f(0.0f, 0.0f, 0.0f),   //specular
	   	   							    128.0f));							 //shininess

	   	alpha = new MyAlpha(1, 5000);
	    alpha.setStartTime(50000000);

		path1 = new MyPositionPathInterpolator(alpha, tran1a, new Transform3D(), new float[]{0, 	1},
			   									   				   new Point3f[]{new Point3f(0,		0,		0),
			   									   				    			 new Point3f(0, 	0, 		0)});

		sphere1 = new Sphere(0.1f, 1, 75, app);
		sphere2 = new Sphere(0.1f, 1, 75, app2);

		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
		    {
				System.exit(0);
		    }
        });

	    JFrame frame = new JFrame();

	    PhysicalEnvironment phe = new PhysicalEnvironment();
		HeadspaceMixer hsm = new HeadspaceMixer(phe);
		hsm.initialize();

		View view = new View();
		ViewPlatform vp = new ViewPlatform();
		view.attachViewPlatform(vp);
		view.setPhysicalBody(new PhysicalBody());
        view.setPhysicalEnvironment(phe);

		GraphicsConfiguration config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().
        								getBestConfiguration(new GraphicsConfigTemplate3D());
        Canvas3D canvas = new Canvas3D(config);
        frame.getContentPane().add("Center", canvas);
        view.addCanvas3D(canvas);

        BoundingSphere bsphere = new BoundingSphere(new Point3d(0.0,0.0,0.0), 10.0);
        DirectionalLight dirLight = new DirectionalLight(new Color3f(1f, 1f, 1f), new Vector3f(0f, 0f, -1f));
        AmbientLight ambLight = new AmbientLight(new Color3f(0.5f, 0.5f, 0.5f));
        dirLight.setInfluencingBounds(bsphere);
        ambLight.setInfluencingBounds(bsphere);

        ColoringAttributes ca = new ColoringAttributes();
        //ca.setShadeModel(ColoringAttributes.SHADE_FLAT);
        app.setColoringAttributes(ca);
        app2.setColoringAttributes(ca);
        tran.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tran.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tran1a.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tran1a.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tran2a.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tran2a.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

       	MouseTranslate mt = new MouseTranslate(tran2a);
        mt.setSchedulingBounds(bsphere);
        path1.setSchedulingBounds(bsphere);
        point.setSchedulingBounds(bsphere);

        MyBehavior myBehavior = new MyBehavior();
        myBehavior.setSchedulingBounds(bsphere);

		Transform3D translate2 = new Transform3D();
		Transform3D all2 = new Transform3D();
		translate2.set(new Vector3f(0.5f, 0.0f, 0.0f));
		all2.set(translate2);
		tran2a.setTransform(all2);

       	scene = new BranchGroup();
        scene.addChild(vp);
        scene.addChild(dirLight);
        scene.addChild(ambLight);
        scene.addChild(point);
        scene.addChild(tran);
        scene.addChild(mt);
        scene.addChild(myBehavior);
        scene.addChild(path1);
        tran.addChild(tran1a);
        tran.addChild(tran2a);
        //tran.addChild(vp);
        tran1a.addChild(sphere1);
        tran2a.addChild(sphere2);
        //tran1b.addChild(sphere1);

        Locale locale = new Locale(universe);
        locale.addBranchGraph(scene);
		System.out.println(universe.getAllLocales());
        //scene.compile();

        frame.setSize(700, 700);
        frame.setVisible(true);
     }
}

