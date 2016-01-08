package change;

public class CreateObjectChange extends Change
{
	private String attributeName;
	private String newObjectId;
	private String className;

	public CreateObjectChange(String targetObjectId, String attributeName, String newObjectId, String className)
	{
		super(targetObjectId);
		this.attributeName = attributeName;
		this.newObjectId = newObjectId;
		this.className = className;
	}
	
	@Override
	public String toString()
	{
		return attributeName + ": new " + className + " (" + newObjectId + ")";
	}

	public String getAttributeName()
	{
		return attributeName;
	}

	public void setAttributeName(String attributeName)
	{
		this.attributeName = attributeName;
	}

	public String getNewObjectId()
	{
		return newObjectId;
	}

	public void setNewObjectId(String newObjectId)
	{
		this.newObjectId = newObjectId;
	}

	public String getClassName()
	{
		return className;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}

}
