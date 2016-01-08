package change;

public class AssociationChange extends Change
{
	private String attributeName;
	private String oldObjectId;
	private String newObjectId;

	public AssociationChange(String targetObjectId, String attributeName, String oldObjectId, String newObjectId)
	{
		super(targetObjectId);
		this.attributeName = attributeName;
		this.oldObjectId = oldObjectId;
		this.newObjectId = newObjectId;
	}
	
	@Override
	public String toString()
	{
		return attributeName + ": " + oldObjectId + " -> " + newObjectId;
	}

	public String getAttributeName()
	{
		return attributeName;
	}

	public void setAttributeName(String attributeName)
	{
		this.attributeName = attributeName;
	}

	public String getOldObjectId()
	{
		return oldObjectId;
	}

	public void setOldObjectId(String oldObjectId)
	{
		this.oldObjectId = oldObjectId;
	}

	public String getNewObjectId()
	{
		return newObjectId;
	}

	public void setNewObjectId(String newObjectId)
	{
		this.newObjectId = newObjectId;
	}

}
