package stroomnetwerk;

import java.util.Set;

public abstract class Onderdeel {
	
	Onderdeel() {}
	
	/**
	 * @creates | result
	 * @post | result != null
	 * @post | result.stream().allMatch(o -> o != null)
	 */
	public abstract Set<? extends Onderdeel> getGekoppeldeOnderdelen();
	
	/**
	 * @post | onderdeel != null || result == false
	 */
	public abstract boolean isGelijkaardigAan(Onderdeel onderdeel);

}
