package com.oracle.coherence.examples.storage;

import com.oracle.coherence.examples.domain.Employee;
import com.oracle.coherence.examples.domain.EmployeeId;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.net.cache.CacheStore;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.LikeFilter;
import com.tangosol.util.Filter;
import com.tangosol.util.filter.*;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * @author Jonathan Knight  2020.09.09
 */
@Component
public class EmployeeCacheStore
        implements CacheStore<EmployeeId, Employee> {

    @Autowired
    private EmployeeRepository repository;

    @Override
    public Employee load(EmployeeId EmployeeId) {
        return repository.findById(EmployeeId.getEmpId())
                .orElse(null);
    }

    @Override
    public void store(EmployeeId EmployeeId, Employee Employee) {
        Employee.setEmpId(EmployeeId.getEmpId());
        repository.save(Employee);
    }

    @Override
    public void erase(EmployeeId EmployeeId) {
        repository.deleteById(EmployeeId.getEmpId());
    }
   
    @SuppressWarnings("unchecked")
	public static Map<EmployeeId, Employee> getEmployeeByDept( String methodName, String parameter ) {
		
		NamedCache cache = (NamedCache) CacheFactory.getCache("employees");
		
		//between filter
		LikeFilter likeFilter = new LikeFilter("getDeptName","%IT%", false);
		EqualsFilter equalsFilter = new EqualsFilter("getLastName","Ajay");
		//... pass N number of filters
		AllFilter filter = new AllFilter(new Filter[]{likeFilter, equalsFilter});
		// getter name of a list in UserBean
	//	Filter filterCriteria = new CollectionsAwareFilter("getAddressList", filter);

		Set<Map.Entry<EmployeeId, Employee>> entries = cache.entrySet(filter);
		Map<EmployeeId, Employee> map = new HashMap<>();

		Employee empData = null;
		for (Map.Entry<EmployeeId, Employee> entry : entries) {
			empData = entry.getValue();
			System.out.println(empData.getEmpId() + " First Name " + empData.getFirstName());
			map.put(entry.getKey(),entry.getValue());
			//break;
		}
		return map;
	}
    
   /* @SuppressWarnings("unchecked")
   	public static Map<EmployeeId, Employee> getValuesbyKey( String from_branch_id, String to_branch_id ) {
   		
   		NamedCache cache = (NamedCache) CacheFactory.getCache("branch_data");
   		
   		//between filter
   		
   		Filter filterfrombrcode = new GreaterEqualsFilter("getBranchCode()", from_branch_id);
   		Filter filtertobrcode = new GreaterEqualsFilter("getBranchCode()", to_branch_id);
                
   		
   		AllFilter filter = new AllFilter(new Filter[] {filterfrombrcode, filtertobrcode});
   		
   		// getter name of a list in UserBean
   	//	Filter filterCriteria = new CollectionsAwareFilter("getAddressList", filter);

   		Set<Map.Entry<BranchId, Branch>> entries = cache.entrySet(filter);
   		Map<EmployeeId, Employee> map = new HashMap<>();

   		Branch branchData = null;
   		for (Map.Entry<EmployeeId, Employee> entry : entries) {
   			branchData = entry.getValue();
   			System.out.println(branchData.getEmpId() + " First Name " + branchData.getBranchName());
   			map.put(entry.getKey(),entry.getValue());
   			//break;
   		}
   		return map;
   	}*/
    
    @Override
    public Map<EmployeeId, Employee> loadAll(Collection<? extends EmployeeId> colKeys) {
        Map<EmployeeId, Employee> map = new HashMap<>();
        for (EmployeeId key : colKeys) {
            Employee value = load(key);
            if (value != null) {
                map.put(key, value);
            }
        }
        return map;
    }

    @Override
    public void eraseAll(Collection<? extends EmployeeId> colKeys) {
        boolean fRemove = true;

        for (Iterator<? extends EmployeeId> iter = colKeys.iterator(); iter.hasNext(); ) {
            erase(iter.next());
            if (fRemove) {
                try {
                    iter.remove();
                }
                catch (UnsupportedOperationException e) {
                    fRemove = false;
                }
            }
        }
    }

    @Override
    public void storeAll(Map<? extends EmployeeId, ? extends Employee> mapEntries) {
        boolean fRemove = true;

        for (Iterator<? extends Map.Entry<? extends EmployeeId, ? extends Employee>> iter = mapEntries.entrySet().iterator();
                iter.hasNext(); ) {
            Map.Entry<? extends EmployeeId, ? extends Employee> entry = iter.next();
            store(entry.getKey(), entry.getValue());
            if (fRemove) {
                try {
                    iter.remove();
                }
                catch (UnsupportedOperationException e) {
                    fRemove = false;
                }
            }
        }
    }

}
