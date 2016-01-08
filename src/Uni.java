import java.util.ArrayList;
import java.util.List;

public class Uni
{
	private String name;
	private List<Student> students = new ArrayList<Student>();

	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public List<Student> getStudents()
	{
		return students;
	}
}
