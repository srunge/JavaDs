package change;

import java.util.ArrayList;
import java.util.List;

public class ChangeList
{
	// TODO user
	// TODO version number(s)
	
	private List<Change> changes = new ArrayList<Change>();
	
	public void addChange(Change change)
	{
		changes.add(change);
	}

	public List<Change> getChanges()
	{
		return changes;
	}
}
