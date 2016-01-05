package org.linkeddatafragments.datasource.blazegraph;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;

import com.bigdata.journal.Options;
import com.bigdata.rdf.sail.BigdataSail;
import com.bigdata.rdf.sail.BigdataSailRepository;
import com.bigdata.rdf.store.AbstractTripleStore;

import org.linkeddatafragments.datasource.DataSourceBase;
import org.linkeddatafragments.datasource.IFragmentRequestProcessor;
import org.linkeddatafragments.exceptions.DataSourceCreationException;
import org.linkeddatafragments.fragments.IFragmentRequestParser;

/**
 * A Blazegraph-based data source.
 * 
 * @author <a href="http://olafhartig.de">Olaf Hartig</a>
 */
public class BlazegraphDataSource extends DataSourceBase
{
    protected final BigdataSailRepository repo;
    protected final boolean repoHasToBeShutDown;
    protected final AbstractTripleStore store;
    protected final BlazegraphBasedTPFRequestProcessor requestProcessor;

    public BlazegraphDataSource( final String title,
                                 final String description,
                                 final Properties props )
                                             throws DataSourceCreationException
    {
        this( title,
              description,
              new BigdataSailRepository( new BigdataSail( check(props,title) ) ),
              true );  // repoHasToBeShutDown=true

        try {
            repo.initialize();
        }
        catch ( Exception e ) {
            throw new DataSourceCreationException( e );
        }

        if ( props.getProperty(Options.BUFFER_MODE,Options.DEFAULT_BUFFER_MODE)
                  .equals("MemStore")
             && props.containsKey("file") )
        {
            loadData( props.getProperty("file") );
        }
    }

    public BlazegraphDataSource( final String title,
                                 final String description,
                                 final BigdataSailRepository repo )
    {
        this( title, description, repo, false ); // repoHasToBeShutDown=false
    }

    protected BlazegraphDataSource( final String title,
                                    final String description,
                                    final BigdataSailRepository repo,
                                    final boolean repoHasToBeShutDown )
    {
        super( title, description );

        this.repo = repo;
        this.repoHasToBeShutDown = repoHasToBeShutDown;
        store = repo.getDatabase();

        // Verify that the given store has properties that are
        // compatible with how we want to use the store here
        // (in particular, it must be guaranteed that matching
        // triples are always returned in the same order). 
        verifyStoreIsSuitable( store );

        requestProcessor = new BlazegraphBasedTPFRequestProcessor( store );
    }

    public static Properties check( final Properties props, final String title )
                                             throws DataSourceCreationException
    {
        final String bufMode = props.getProperty( Options.BUFFER_MODE,
                                                  Options.DEFAULT_BUFFER_MODE );

        if ( ! bufMode.equals("MemStore") && props.containsKey(Options.FILE) )
        {
            final String fname = props.getProperty( Options.FILE );
            final File file = new File( fname );

            if ( ! file.exists() )
                throw new DataSourceCreationException( title,
                                               "Blazegraph journal does " +
                                               "not exist (" + fname + ")." );

            if ( ! file.canRead() )
                throw new DataSourceCreationException( title,
                                               "Blazegraph journal cannot " +
                                               "be read (" + fname + ")." );
        }

        return props;
    }

    public void verifyStoreIsSuitable( final AbstractTripleStore store )
    {
        if ( store.isQuads() ) {
            throw new IllegalArgumentException(
                          "The given store must be for triples (not quads)." );
        }
        
        if ( store.isStatementIdentifiers() ) {
            throw new IllegalArgumentException(
                                "The given store must not support RDR/SIDS." );
        }

//        if ( ! store.isReadOnly() ) {
//            throw new IllegalArgumentException(
//                                        "The given store must be read-only." );
//        }

        if ( store.isJustify() ) {
            throw new IllegalArgumentException(
              "The given store must not support justication of entailments." );
        }
    }

    @Override
    public void close()
    {
System.out.println( "SHUTTING DOWN THE REPO" );
        if ( repoHasToBeShutDown ) {
            try {
                repo.shutDown();                
            }
            catch ( RepositoryException e ) {
                throw new IllegalStateException( e );
            }
        }
    }

    protected void loadData( final String filename )
                                             throws DataSourceCreationException
    {
        if ( filename == null )
            return;

        final RepositoryConnection cxn;
        try {
            cxn = repo.getConnection();
            cxn.begin();
        }
        catch ( RepositoryException e ) {
            throw new DataSourceCreationException( e );
        }

        try {
            final String baseURL = "http://db.uwaterloo.ca/~galuc/wsdbm/";
            final InputStream is = new FileInputStream( filename );
            final Reader reader = new InputStreamReader(
                                                 new BufferedInputStream(is) );
            cxn.add( reader, baseURL, RDFFormat.forFileName(filename) );
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

    @Override
    public IFragmentRequestParser getRequestParser()
    {
        return new TPFRequestParserForBlazegraph( store.getLexiconRelation() );
    }

    @Override
    public IFragmentRequestProcessor getRequestProcessor()
    {
        return requestProcessor;
    }

}
