import java.util.LinkedList;
public class vertex{
	LinkedList<Hex> vertex = new LinkedList<Hex>();
	
	public vertex(){

	}
	public void add(Hex side){
			vertex.add(side);
	}
	public Hex getside(int postion){
		if(postion < vertex.size()) {
			return vertex.get(postion);
		}
		else {
			return null;
		}
	}	

}
	
