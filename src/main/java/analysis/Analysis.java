/**
 * @author Raul - raulcf@csail.mit.edu
 *
 */
package analysis;

import analysis.modules.Cardinality;
import analysis.modules.DataType;
import analysis.modules.Signature;

public interface Analysis {

	public DataProfile getProfile();
	public DataType getDataType();
	public Signature getSignature();
	public Cardinality getCardinality();
	
}
