import java.awt.*;
import java.awt.event.*;
import com.sun.j3d.utils.geometry.*;  		// for cone
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.universe.*;  	    // for SimpleUniverse
import javax.media.j3d.*;            		// for Java3D API classes
import javax.vecmath.*;                     // for Point3d etc.
import javax.swing.*;
import java.io.*;
import java.util.Enumeration;
import com.sun.j3d.audioengines.headspace.*;
import com.sun.j3d.utils.image.TextureLoader;



class MyFrame2 extends JFrame
{
   JButton start = new JButton("start");

   SimpleUniverse simpleU;
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
   PointSound point;
   Texture text;
   Texture text2;


   class MyBehavior extends Behavior
   {
	   private WakeupCondition wakeupCondition;

   	   public MyBehavior()
   	   {
		   wakeupCondition = new WakeupOnCollisionEntry(sphere1);// WakeupOnCollisionEntry.USE_GEOMETRY);
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
   			point.setEnable(true);
   			wakeupOn(wakeupCondition);
   		}
	}

   public MyFrame2() throws Exception
   {
	   point = new PointSound(new MediaContainer("file:./meow08.wav"), 1.0f, 0.0f, 0.0f, 0.0f);
	   point.setEnable(false);

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

	   sphere1 = new Sphere(0.175f, 1, 75, app);
	   sphere2 = new Sphere(0.175f, 1, 75, app2);

	   Container con = getContentPane();
	   JPanel panel = new JPanel();
	   con.add("South", panel);
	   panel.add(start);

	   addWindowListener(new WindowAdapter()
	   {
	   		public void windowClosing(WindowEvent e)
	   	    {
	   			System.exit(0);
	   	    }
        });

	    addWindowListener(new WindowAdapter()
        {
			public void windowClosing(WindowEvent e)
            {
				System.exit(0);
            }
        });

        start.addActionListener(new ActionListener()
        {
			private boolean started = false;
            public void actionPerformed(ActionEvent e)
            {
				if (!started)
                {
					simpleU.addBranchGraph(scene);
                    started = true;
                }
            }
        });

        TextureLoader load = new TextureLoader("viette1a.jpg", this);
        text = load.getTexture();
        app.setTexture(text);

        TextureLoader load2 = new TextureLoader("freakybeebs.jpg", this);
		text2 = load2.getTexture();
        app2.setTexture(text2);

        TextureAttributes att = new TextureAttributes();
        att.setTextureMode(TextureAttributes.REPLACE);
        app.setTextureAttributes(att);
        app2.setTextureAttributes(att);

        TexCoordGeneration textgen = new TexCoordGeneration(TexCoordGeneration.SPHERE_MAP,
        													TexCoordGeneration.TEXTURE_COORDINATE_2);

        TexCoordGeneration textgen2 = new TexCoordGeneration(TexCoordGeneration.SPHERE_MAP,
        													TexCoordGeneration.TEXTURE_COORDINATE_3);
        app.setTexCoordGeneration(textgen2);
        app2.setTexCoordGeneration(textgen);


        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(config);
        con.add("Center", canvas);

        BoundingSphere bsphere = new BoundingSphere(new Point3d(0.0,0.0,0.0), 10.0);
        DirectionalLight dirLight = new DirectionalLight(new Color3f(1f, 1f, 1f), new Vector3f(0f, 0f, -1f));
        AmbientLight ambLight = new AmbientLight(new Color3f(0.5f, 0.5f, 0.5f));
        dirLight.setInfluencingBounds(bsphere);
        ambLight.setInfluencingBounds(bsphere);


        ColoringAttributes ca = new ColoringAttributes();
		app.setColoringAttributes(ca);
		app2.setColoringAttributes(ca);
		tran.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tran.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tran1a.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tran1a.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tran2a.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tran2a.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		point.setCapability(PointSound.ALLOW_POSITION_WRITE);
		point.setCapability(PointSound.ALLOW_POSITION_READ);
		point.setCapability(PointSound.ALLOW_ENABLE_WRITE);
		point.setCapability(PointSound.ALLOW_ENABLE_READ);


		MouseTranslate mt = new MouseTranslate(tran2a);
		mt.setSchedulingBounds(bsphere);
		path1.setSchedulingBounds(bsphere);
		point.setSchedulingBounds(bsphere);

		MyBehavior myBehavior = new MyBehavior();
		myBehavior.setSchedulingBounds(bsphere);

		Transform3D translate2 = new Transform3D();
		Transform3D all2 = new Transform3D();
		translate2.set(new Vector3f(0.75f, 0.0f, 0.0f));
		all2.set(translate2);
		tran2a.setTransform(all2);

		PhysicalEnvironment phe = new PhysicalEnvironment();
		HeadspaceMixer hsm = new HeadspaceMixer(phe);
		hsm.initialize();

		simpleU = new SimpleUniverse(canvas);
        simpleU.getViewingPlatform().setNominalViewingTransform();
        View view = simpleU.getViewer().getView();
        view.setPhysicalEnvironment(phe);

		scene = new BranchGroup();
		scene.addChild(dirLight);
		scene.addChild(ambLight);
		scene.addChild(point);
		scene.addChild(tran);
		scene.addChild(mt);
		scene.addChild(myBehavior);
		scene.addChild(path1);
		tran.addChild(tran1a);
		tran.addChild(tran2a);
		tran1a.addChild(sphere1);
		tran2a.addChild(sphere2);
        //tran1b.addChild(sphere1);

        scene.compile();

        setSize(1000, 750);
        setVisible(true);
     }
}

