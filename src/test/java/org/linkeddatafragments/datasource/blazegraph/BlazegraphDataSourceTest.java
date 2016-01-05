package org.linkeddatafragments.datasource.blazegraph;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.bigdata.journal.Options;
import com.bigdata.rdf.model.BigdataValue;
import com.bigdata.rdf.sail.BigdataSail;
import com.bigdata.rdf.sail.BigdataSailRepository;
import com.bigdata.rdf.store.AbstractTripleStore;

import org.linkeddatafragments.exceptions.DataSourceCreationException;
import org.linkeddatafragments.util.TriplePatternElementParser;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;

import test.java.org.linkeddatafragments.datasource.DataSourceTest;

/**
 * @author <a href="http://olafhartig.de">Olaf Hartig</a>
 */
public class BlazegraphDataSourceTest
    extends DataSourceTest<BigdataValue,String,String>
{
    protected static BigdataSailRepository repo;
    protected static TriplePatternElementParserForBlazegraph tpeParser;

    @Override
    protected TriplePatternElementParser<BigdataValue,String,String>
                                               getTriplePatternElementParser()
    {
        return tpeParser;
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        final Properties props = new Properties();
        props.setProperty( BigdataSail.Options.TRUTH_MAINTENANCE, "false" );
        props.setProperty( AbstractTripleStore.Options.TRIPLES_MODE, "true" );
        props.setProperty( AbstractTripleStore.Options.AXIOMS_CLASS, "com.bigdata.rdf.axioms.NoAxioms" );
        props.setProperty( AbstractTripleStore.Options.JUSTIFY, "false" );
        props.setProperty( AbstractTripleStore.Options.STATEMENT_IDENTIFIERS, "false" );
        props.setProperty( AbstractTripleStore.Options.TEXT_INDEX, "false" );
        props.setProperty( Options.BUFFER_MODE, "MemStore" );

        try {
          repo = new BigdataSailRepository( new BigdataSail(props) );

          repo.initialize();

          loadTestData();

          setDatasource( new BlazegraphDataSource("Blzgtest","Blzgtest",repo) );
        }
        catch ( Exception e ) {
            e.printStackTrace( System.err );
            throw e;
        }

        tpeParser = new TriplePatternElementParserForBlazegraph(
                                     repo.getDatabase().getLexiconRelation() );
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
        repo.shutDown();
    }

    protected static void loadTestData() throws DataSourceCreationException
    {
        final RepositoryConnection cxn;
        try {
            cxn = repo.getConnection();
            cxn.begin();
        }
        catch ( RepositoryException e ) {
            throw new DataSourceCreationException( e );
        }

        try {
            final String baseURL = "http://example.org/";
            final InputStream is = ClassLoader.getSystemResourceAsStream("demo.nt");
            final Reader reader = new InputStreamReader(
                                                 new BufferedInputStream(is) );
            cxn.add( reader, baseURL, RDFFormat.NTRIPLES );
            cxn.commit();
        }
        catch ( Exception e ) {
            try {
                cxn.rollback();
            }
            catch ( Exception e2 ) {
                // ignore
            }
            throw new DataSourceCreationException( e );
        }
        finally {
            // close the repository connection
            try {
                cxn.close();
            }
            catch ( Exception e2 ) {
                // ignore
            }
        }
    }

    @Before
    public void setUp() throws Exception {

    }
    
    @After
    public void tearDown() throws Exception {
    }

}
