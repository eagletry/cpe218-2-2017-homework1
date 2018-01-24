import java.util.Stack;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import java.net.URL;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultTreeCellRenderer;

public class Homework1 {
	public static Stack<Character> Bomb = new Stack<Character>();
	public static node tree;
	public static node temp;
	public static String Sc;
	public static void main(String[] args)
	{
// Begin of arguments input sample
//		if (args.length > 0) {
//			String input = args[0];
//			if (input.equalsIgnoreCase("251-*32*+")) {
//				System.out.println("(2*(5-1))+(3*2)=14");
//			}
//		}


		String input ="251-*32*+";
		if(args.length>0)input= args[0];
		for(int i=0;i<input.length();i++)
		{
			Bomb.add(input.charAt(i));
		}
		tree = new node(Bomb.pop());
		infix(tree);
		calculate(tree);

		System.out.println(inorder(tree)+"="+calculate(tree));



		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				guitree.createAndShowGUI();
			}
		});
		// End of arguments input sample
		// TODO: Implement your project here
	}

	public static String inorder(node d){
		String a = "";
		if(d.nop == '+'){
			if(d!=temp)a += "(";
			//check "()" if d is root node
			a += inorder(d.left);
			a += "+";
			a += inorder(d.right);

			if(d!=temp)a +=")";
		}
		else if(d.nop == '-'){
			if(d!=temp)a +="(";
			a += inorder(d.left);
			a += "-";
			a += inorder(d.right);

			if(d!=temp)a += ")";
		}else if(d.nop == '*'){
			if(d!=temp)a += "(";
			a += inorder(d.left);
			a += "*";
			a += inorder(d.right);

			if(d!=temp)a +=")";
		}else if(d.nop == '/'){
			if(d!=temp)a += "(";
			a += inorder(d.left);
			a += "/";
			a += inorder(d.right);

			if(d!=temp)a += ")";
		}
		else
		{
			a += d.nop;

		}
		return a;
	}

	public static void infix(node b) {
		//add to tree
		if(b.nop =='+'||b.nop == '-'||b.nop =='*'||b.nop =='/' ){
			b.right=new node(Bomb.pop());
			infix(b.right);
			b.left=new node(Bomb.pop());
			infix(b.left);
		}
	}

	public static int calculate(node c){

		if(c.nop =='+'){
			return calculate(c.left) + calculate(c.right);

		}
		if(c.nop =='-'){
			return calculate(c.left) - calculate(c.right);
		}
		if(c.nop =='*'){
			return calculate(c.left) * calculate(c.right);
		}
		if(c.nop =='/'){
			return calculate(c.left) / calculate(c.right);
		}
		else return Integer.parseInt(c.nop.toString()); // change to integer
	}
}

class node {
	Character nop;
	node(char a)
	//check input
	{
		nop=a;
	}
	node left;
	node right;
	public String toString() {
		return nop.toString();
	}
}

class guitree extends JPanel
		implements TreeSelectionListener {
	private JEditorPane htmlPane;
	private JTree Jtree;
	private URL helpURL;
	private static boolean DEBUG = false;

	//Optionally play with line styles.  Possible values are
	//"Angled" (the default), "Horizontal", and "None".
	private static boolean playWithLineStyle = false;
	private static String lineStyle = "Horizontal";

	//Optionally set the look and feel.
	private static boolean useSystemLookAndFeel = false;

	public guitree() {
		super(new GridLayout(1,0));

		//Create the nodes.
		DefaultMutableTreeNode top =
				new DefaultMutableTreeNode(Homework1.tree);

		CreateNode(top);
		//Create a tree that allows one selection at a time.
		Jtree = new JTree(top);
		Jtree.getSelectionModel().setSelectionMode
				(TreeSelectionModel.SINGLE_TREE_SELECTION);



		//Listen for when the selection changes.
		Jtree.addTreeSelectionListener(this);

		if (playWithLineStyle) {
			System.out.println("line style = " + lineStyle);
			Jtree.putClientProperty("JTree.lineStyle", lineStyle);
		}

		//Create the scroll pane and add the tree to it.
		JScrollPane treeView = new JScrollPane(Jtree);

		//Create the HTML viewing pane.
		htmlPane = new JEditorPane();
		htmlPane.setEditable(false);
		JScrollPane htmlView = new JScrollPane(htmlPane);

		//Add the scroll panes to a split pane.
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(treeView);
		splitPane.setBottomComponent(htmlView);

		Dimension minimumSize = new Dimension(100, 50);
		htmlView.setMinimumSize(minimumSize);
		treeView.setMinimumSize(minimumSize);
		splitPane.setDividerLocation(100);
		splitPane.setPreferredSize(new Dimension(500, 300));

		ImageIcon leafIcon = createImageIcon("Icon.gif");
		if (leafIcon != null) {
			DefaultTreeCellRenderer renderer =
					new DefaultTreeCellRenderer();
			renderer.setOpenIcon(leafIcon);
			renderer.setClosedIcon(leafIcon);

			Jtree.setCellRenderer(renderer);
		}



		//Add the split pane to this panel.
		add(splitPane);
	}

	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = guitree.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	/** Required by TreeSelectionListener interface. */
	public void valueChanged(TreeSelectionEvent e) {

		DefaultMutableTreeNode g = (DefaultMutableTreeNode)
				Jtree.getLastSelectedPathComponent();

		node n =(node)g.getUserObject();
		if (g == null) return;
		Homework1.temp=n;
		htmlPane.setText(Homework1.inorder(n)+"="+Homework1.calculate(n));

	}

	private class BookInfo {
		public String bookName;
		public URL bookURL;

		public BookInfo(String book, String filename) {
			bookName = book;
			bookURL = getClass().getResource(filename);
			if (bookURL == null) {
				System.err.println("Couldn't find file: "
						+ filename);
			}
		}

		public String toString() {
			return bookName;
		}
	}


	private void displayURL(URL url) {
		try {
			if (url != null) {
				htmlPane.setPage(url);
			} else { //null url
				htmlPane.setText("File Not Found");
				if (DEBUG) {
					System.out.println("Attempted to display a null URL.");
				}
			}
		} catch (IOException e) {
			System.err.println("Attempted to read a bad URL: " + url);
		}
	}


	public void CreateNode(DefaultMutableTreeNode n)
	{
		node x=(node)n.getUserObject();
		if(x.left!=null)
		{
			DefaultMutableTreeNode l = new DefaultMutableTreeNode(x.left);
			n.add(l);
			CreateNode(l);
		}
		if(x.right!=null)
		{
			DefaultMutableTreeNode r = new DefaultMutableTreeNode(x.right);
			n.add(r);
			CreateNode(r);
		}
	}

	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event dispatch thread.
	 */
	public static void createAndShowGUI() {
		if (useSystemLookAndFeel) {
			try {
				UIManager.setLookAndFeel(
						UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				System.err.println("Couldn't use system look and feel.");
			}
		}

		//Create and set up the window.
		JFrame frame = new JFrame("TreeDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Add content to the window.
		frame.add(new guitree());

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}


}





