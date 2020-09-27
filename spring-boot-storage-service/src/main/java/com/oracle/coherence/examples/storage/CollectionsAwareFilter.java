package com.oracle.coherence.examples.storage;

import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.filter.ComparisonFilter;
import java.util.Collection;
/**
 * <pre>
 * Filter which tests a Collection or Object array value returned from a method invocation for evaluation of filters on attributes of Collection objects.
 *
 *
 * @author Hemambara Vamsi Kotari
 */
public class CollectionsAwareFilter extends ComparisonFilter {
    /**
     * Created to support default constructor mechanism
     */
    public CollectionsAwareFilter() {
    }
    /**
     * Takes methodName and convert it to value extractor. Method name should be the name of the method that retrieves {@code Collection} from main object.
     *
     * @param sMethod - methodName
     * @param filter - {@link Filter}
     */
    public CollectionsAwareFilter(String sMethod, Filter filter) {
        super(sMethod, filter);
        this.init(filter);
    }
    /**
     * {@code ValueExtractor} retrieves {@code Collection} from main object.
     *
     * @param extractor - {@link ValueExtractor}
     * @param filter - {@link Filter}
     */
    public CollectionsAwareFilter(ValueExtractor extractor, Filter filter) {
        super(extractor, filter);
        this.init(filter);
    }
    /**
     * Make sure {@link Filter} is NOT NULL. It does not make any sense to parse through collection if filter is null.
     *
     * @param filter
     */
    protected void init(Filter filter){
      if (null == filter) {
        throw new IllegalArgumentException("Filter argument must not be null"); 
      } 
    }
    /**
     * Evaluate the collection, iterate through them and apply filters.
     *
     * @param oExtracted - Extracted collection from main object.
     * @return - True - If any filter pass.
     *          - False - If none of the filter pass.
     */
    @Override
    protected boolean evaluateExtracted(Object oExtracted) {
        Collection aoExtracted = (Collection)oExtracted;
        Filter filter = (Filter) this.getValue();
        for(Object object : aoExtracted){
            if(filter.evaluate(object)){
                return true;
            }
        }
        return false;
    }
}