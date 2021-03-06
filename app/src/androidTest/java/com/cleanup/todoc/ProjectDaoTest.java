package com.cleanup.todoc;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cleanup.todoc.database.database.TodocDatabase;
import com.cleanup.todoc.model.Project;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ProjectDaoTest {

    //for data
    private TodocDatabase database;

    /**
     * each test is launched in sync task
     */
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    /**
     *for create a database instance in memory
     */
    @Before
    public void initDb() {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                TodocDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() {
        database.close();
    }

    //data to use in tests for Project table
    /**
     * create project test
     */

    private static long PROJECT_ID_1 = 1;
    private static Project DEMO_1_PROJECT = new Project(PROJECT_ID_1, "Projet Tartampion", 0xFFEADAD1);
    private static Project FALSE_DEMO_PROJECT_WITH_ID_1 = new Project(PROJECT_ID_1, "False Projet Tartampion", 0xFFB5CDBA);

    /**
     * At first time search with :
     * @Query("SELECT * FROM Project")
     *     LiveData<Project> getProjects();
     */
//    @Test
//    public void insertProjectWithSuccess() {
//        this.database.projectDao().createProject(DEMO_1_PROJECT);
//        assertNotNull(this.database.projectDao());
//    }
//
//    @Test
//    public void getProjectWithSuccess() throws InterruptedException {
//        this.database.projectDao().createProject(DEMO_1_PROJECT);
//        Project project = LiveDataTestUtils.getValue(this.database.projectDao().getProjects());
//        assertEquals(project.getId(), PROJECT_ID_1);
//        assertEquals(project.getName(), DEMO_1_PROJECT.getName());
//        assertEquals(project.getColor(), DEMO_1_PROJECT.getColor());
//    }

    /**
     * In a second time search with
     * @Query("SELECT * FROM Project")
     *     LiveData<List<Project>> getProjects();
     */

    @Test
    public void getProjectWhenNoProjectInsertedShouldReturnIsEmpty() throws InterruptedException
    {
        List<Project> projects = LiveDataTestUtils.getValue(this.database.projectDao().getProjects());
        assertTrue(projects.isEmpty());
    }

    @Test
    public void createAProjectWithSuccess() throws InterruptedException
    {
        this.database.projectDao().createProject(DEMO_1_PROJECT);
        List<Project> projects = new ArrayList<>(LiveDataTestUtils.getValue(this.database.projectDao().getProjects()));
        assertEquals(projects.size(), 1);
    }

    @Test
    public void getIdNameAndColorWithSuccess() throws InterruptedException {
        this.database.projectDao().createProject(DEMO_1_PROJECT);
        List<Project> projects = new ArrayList<>(LiveDataTestUtils.getValue(this.database.projectDao().getProjects()));
        assertEquals(projects.get(0).getId(), PROJECT_ID_1);
        assertEquals(projects.get(0).getName(), DEMO_1_PROJECT.getName());
        assertEquals(projects.get(0).getColor(), DEMO_1_PROJECT.getColor());
    }

//    /**
//     * not here but in repository
//     * @throws InterruptedException
//     */
//    @Test(expected = IndexOutOfBoundsException.class)
//    public void getAProjectOutOfRange() throws InterruptedException {
//        this.database.projectDao().createProject(DEMO_1_PROJECT);
//        List<Project> projects = new ArrayList<>(LiveDataTestUtils.getValue(this.database.projectDao().getProjects()));
//        assertEquals(projects.get(1).getId(), PROJECT_ID_1);
//    }

    /**
     * dont find how test this
     * @throws InterruptedException
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void differentProjectsCannotHaveSameId() throws InterruptedException {
        this.database.projectDao().createProject(DEMO_1_PROJECT);
        this.database.projectDao().createProject(FALSE_DEMO_PROJECT_WITH_ID_1);
        List<Project> projects = new ArrayList<>(LiveDataTestUtils.getValue(this.database.projectDao().getProjects()));
        assertEquals(projects.get(0).getId(), PROJECT_ID_1);
        assertNull(projects.get(1).getName());
    }

}
