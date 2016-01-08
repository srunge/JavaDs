package diff;

import org.junit.Assert;
import org.junit.Test;

import change.AssociationChange;
import change.Change;
import change.ChangeList;
import change.CreateObjectChange;
import change.ValueChange;
import model.Model;

class TestClass
{
	private int x;
	public double y;
	protected String z;
	
	TestClass(int x, double y, String z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
}

class TestClass2
{
	private TestClass testClass;
	private int x;
	
	TestClass2(TestClass testClass, int x)
	{
		this.testClass = testClass;
		this.x = x;
	}

	public void setTestClass(TestClass testClass)
	{
		this.testClass = testClass;
	}
}

public class DsDifferTest
{
	private void assertContainsValueChange(ChangeList changeList, String objectId, String attributeName, Object oldValue, Object newValue)
	{
		boolean found = false;
		for (Change change : changeList.getChanges())
		{
			if (change instanceof ValueChange)
			{
				ValueChange castChange = (ValueChange) change;
				if (castChange.getTargetObjectId().equals(objectId)
						&& castChange.getAttributeName().equals(attributeName)
						&& ( oldValue == null && castChange.getOldValue() == null || castChange.getOldValue().equals(oldValue) )
						&& ( newValue == null && castChange.getNewValue() == null || castChange.getNewValue().equals(newValue)) )
				{
					found = true;
					break;
				}
			}
		}
		Assert.assertTrue("Expected value change: " + oldValue + " -> " + newValue, found);
	}
	
	private void assertContainsCreateObjectChange(ChangeList changeList, String objectId, String attributeName, String newObjectId)
	{
		boolean found = false;
		for (Change change : changeList.getChanges())
		{
			if (change instanceof CreateObjectChange)
			{
				CreateObjectChange castChange = (CreateObjectChange) change;
				if (castChange.getTargetObjectId().equals(objectId)
						&& castChange.getAttributeName().equals(attributeName)
						&& castChange.getNewObjectId().equals(newObjectId))
				{
					found = true;
					break;
				}
			}
		}
		Assert.assertTrue(found);
	}
	
	private void assertContainsAssociationChange(ChangeList changeList, String objectId, String attributeName, String oldObjectId, String newObjectId)
	{
		boolean found = false;
		for (Change change : changeList.getChanges())
		{
			if (change instanceof AssociationChange)
			{
				AssociationChange castChange = (AssociationChange) change;
				if (castChange.getTargetObjectId().equals(objectId)
						&& castChange.getAttributeName().equals(attributeName)
						&& castChange.getOldObjectId().equals(oldObjectId)
						&& castChange.getNewObjectId().equals(newObjectId))
				{
					found = true;
					break;
				}
			}
		}
		Assert.assertTrue(found);
	}
	
	@Test
	public void testNoPrimitiveChanges() throws Exception
	{
		Model model1 = new Model(new TestClass(12, 13.0, "test"));
		Model model2 = new Model(new TestClass(12, 13.0, "test"));
		DsDiffer differ = new DsDiffer(model1, model2);
		differ.diff();
		Assert.assertTrue(differ.getChangeList().getChanges().isEmpty());
	}
	
	@Test
	public void testPrimitiveChanges() throws Exception
	{
		Model model1 = new Model(new TestClass(12, 13.0, "test"));
		Model model2 = new Model(new TestClass(14, 12.0, "tst"));
		DsDiffer differ = new DsDiffer(model1, model2);
		differ.diff();
		Assert.assertTrue(differ.getChangeList().getChanges().size() == 3);
		assertContainsValueChange(differ.getChangeList(), "aaa.1", "x", 12, 14);
		assertContainsValueChange(differ.getChangeList(), "aaa.1", "y", 13.0, 12.0);
		assertContainsValueChange(differ.getChangeList(), "aaa.1", "z", "test", "tst");
	}
	
	@Test
	public void testNoComplexChanges() throws Exception
	{
		Model model1 = new Model(new TestClass2(null, 12));
		Model model2 = new Model(new TestClass2(null, 12));
		DsDiffer differ = new DsDiffer(model1, model2);
		differ.diff();
		Assert.assertTrue(differ.getChangeList().getChanges().isEmpty());
	}
	
	@Test
	public void testComplexChanges() throws Exception
	{
		Model model1 = new Model(new TestClass2(null, 12));
		TestClass2 targetRoot = new TestClass2(null, 12);
		Model model2 = new Model(targetRoot);
		TestClass newObject = new TestClass(42, 12.0, "test");
		targetRoot.setTestClass(newObject);
		DsDiffer differ = new DsDiffer(model1, model2);
		differ.diff();
		Assert.assertTrue(differ.getChangeList().getChanges().size() == 5);
		assertContainsCreateObjectChange(differ.getChangeList(), "aaa.1", "testClass", "aaa.2");
		assertContainsAssociationChange(differ.getChangeList(), "aaa.1", "testClass", "", "aaa.2");
		assertContainsValueChange(differ.getChangeList(), "aaa.2", "x", null, 42);
		assertContainsValueChange(differ.getChangeList(), "aaa.2", "y", null, 12.0);
		assertContainsValueChange(differ.getChangeList(), "aaa.2", "z", null, "test");
	}
}
