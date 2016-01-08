package org.linkeddatafragments.datasource.blazegraph;

import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.Map1Iterator;

import com.bigdata.rdf.model.BigdataStatement;
import com.bigdata.rdf.store.BigdataStatementIterator;

import java.io.Closeable;

import org.linkeddatafragments.fragments.tpf.TriplePatternFragmentBase;

/**
 * Implementation of {@link TriplePatternFragment}.
 * 
 * @author <a href="http://olafhartig.de">Olaf Hartig</a>
 */
public class BlazegraphBasedTPF extends TriplePatternFragmentBase
{
    protected final MyStmtIterator myStmtIt;

    /**
     * Creates an empty Triple Pattern Fragment.
     */
    public BlazegraphBasedTPF( final String fragmentURL,
                               final String datasetURL )
    {
        this( null, 0L, fragmentURL, datasetURL, 1, true );
    }

    /**
     * Creates an empty Triple Pattern Fragment page.
     */
    public BlazegraphBasedTPF( final String fragmentURL,
                               final String datasetURL,
                               final long pageNumber,
                               final boolean isLastPage )
    {
        this( null, 0L, fragmentURL, datasetURL, pageNumber, isLastPage );
    }

    /**
     * Creates a new Triple Pattern Fragment.
     * @param triples the triples (possibly partial)
     * @param totalSize the total size
     */
    public BlazegraphBasedTPF( final BigdataStatementIterator blzgStmtIt,
                               long totalSize,
                               final String fragmentURL,
                               final String datasetURL,
                               final long pageNumber,
                               final boolean isLastPage )
    {
        super( totalSize, fragmentURL, datasetURL, pageNumber, isLastPage );

        if ( blzgStmtIt != null )
            myStmtIt = new MyStmtIterator( blzgStmtIt );
        else
            myStmtIt = null;
    }

    @Override
    protected StmtIterator getNonEmptyStmtIterator()
    {
        if ( myStmtIt == null )
            throw new IllegalStateException();

        if ( ! myStmtIt.hasNext() )
            throw new IllegalStateException();

        return myStmtIt;
    }


    static public class MyStmtIterator
        extends Map1Iterator<BigdataStatement,Statement>
        implements StmtIterator, Closeable
    {
        protected final BigdataStatementIterator blzgStmtIt;
        private boolean closed = false;

        public MyStmtIterator( final BigdataStatementIterator blzgStmtIt )
        {
            super( BigdataStatementToJenaStatementMapper.getInstance(),
                   blzgStmtIt );

            this.blzgStmtIt = blzgStmtIt;
        }

        @Override
        public Statement nextStatement()
        {
            return next();
        }

        @Override
        public boolean hasNext()
        {
            if ( closed )
                return false;

            if ( ! blzgStmtIt.hasNext() ) {
                close();
                return false;
            }
            else {
                return true;
            }
        }
        
        @Override
        public void close()
        {
            if ( closed )
                return;

            blzgStmtIt.close();
            super.close();
            closed = true;
        }

        public boolean isClosed()
        {
            return closed;
        }

    } // end of MyStmtIterator

}
