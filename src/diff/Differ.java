package diff;

import change.ChangeList;
import model.Model;

public abstract class Differ
{
	private Model sourceModel;
	private Model targetModel;
	private ChangeList changeList;
	
	public Differ(Model sourceModel, Model targetModel)
	{
		this.sourceModel = sourceModel;
		this.targetModel = targetModel;
		this.changeList = new ChangeList();
	}
	
	public void diff() throws DiffException
	{
		// empty
	}

	public Model getSourceModel()
	{
		return sourceModel;
	}

	public void setSourceModel(Model sourceModel)
	{
		this.sourceModel = sourceModel;
	}

	public Model getTargetModel()
	{
		return targetModel;
	}

	public void setTargetModel(Model targetModel)
	{
		this.targetModel = targetModel;
	}

	public ChangeList getChangeList()
	{
		return changeList;
	}

	public void setChangeList(ChangeList changeList)
	{
		this.changeList = changeList;
	}
}
