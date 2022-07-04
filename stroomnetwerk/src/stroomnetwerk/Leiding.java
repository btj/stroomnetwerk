package stroomnetwerk;

import java.util.Objects;
import java.util.Set;

import logicalcollections.LogicalSet;

/**
 * @invar | 0 < getDebiet()
 * @invar | (getBronknoop() == null) == (getDoelknoop() == null)
 * @invar | getBronknoop() == null || getBronknoop().getUitgaandeLeidingen().contains(this)
 * @invar | getDoelknoop() == null || getDoelknoop().getInkomendeLeidingen().contains(this)
 */
public class Leiding extends Onderdeel {
	
	/**
	 * @invar | 0 < debiet
	 * @invar | (bronknoop == null) == (doelknoop == null)
	 * @invar | bronknoop == null || bronknoop.uitgaandeLeidingen.contains(this)
	 * @invar | doelknoop == null || doelknoop.inkomendeLeidingen.contains(this)
	 */
	int debiet;
	/**
	 * @peerObject
	 */
	Knoop bronknoop;
	/**
	 * @peerObject
	 */
	Knoop doelknoop;
	
	public int getDebiet() { return debiet; }
	
	/**
	 * @peerObject
	 */
	public Knoop getBronknoop() { return bronknoop; }

	/**
	 * @peerObject
	 */
	public Knoop getDoelknoop() { return doelknoop; }

	/**
	 * @throws IllegalArgumentException | debiet <= 0
	 * @post | getDebiet() == debiet
	 * @post | getBronknoop() == null
	 * @post | getDoelknoop() == null
	 */
	public Leiding(int debiet) {
		if (debiet <= 0)
			throw new IllegalArgumentException("`debiet` is not greater than zero");
		this.debiet = debiet;
	}
	
	/**
	 * @pre | getBronknoop() == null
	 * @pre | bronknoop != null
	 * @pre | doelknoop != null
	 * @mutates_properties | getBronknoop(), getDoelknoop(), bronknoop.getUitgaandeLeidingen(), doelknoop.getInkomendeLeidingen()
	 * @post | getBronknoop() == bronknoop
	 * @post | getDoelknoop() == doelknoop
	 * @post | getBronknoop().getUitgaandeLeidingen().equals(LogicalSet.plus(old(bronknoop.getUitgaandeLeidingen()), this))
	 * @post | getDoelknoop().getInkomendeLeidingen().equals(LogicalSet.plus(old(doelknoop.getInkomendeLeidingen()), this))
	 */
	public void koppelAan(Knoop bronknoop, Knoop doelknoop) {
		this.bronknoop = bronknoop;
		this.doelknoop = doelknoop;
		bronknoop.uitgaandeLeidingen.add(this);
		doelknoop.inkomendeLeidingen.add(this);
	}
	
	/**
	 * @pre | getBronknoop() != null
	 * @mutates_properties | getBronknoop(), getDoelknoop(), getBronknoop().getUitgaandeLeidingen(), getDoelknoop().getInkomendeLeidingen()
	 * @post | getBronknoop() == null
	 * @post | getDoelknoop() == null
	 * @post | old(getBronknoop()).getUitgaandeLeidingen().equals(LogicalSet.minus(old(getBronknoop().getUitgaandeLeidingen()), this))
	 * @post | old(getDoelknoop()).getInkomendeLeidingen().equals(LogicalSet.minus(old(getDoelknoop().getInkomendeLeidingen()), this))
	 */
	public void ontkoppel() {
		bronknoop.uitgaandeLeidingen.remove(this);
		doelknoop.inkomendeLeidingen.remove(this);
		bronknoop = null;
		doelknoop = null;
	}
	
	/**
	 * @creates | result
	 * @post | Objects.equals(result,
	 *       |         getBronknoop() == null
	 *       |     ?
	 *       |         Set.of()
	 *       |     :
	 *       |         Set.of(getBronknoop(), getDoelknoop())
	 *       | )
	 */
	@Override
	public Set<? extends Onderdeel> getGekoppeldeOnderdelen() {
	    if (bronknoop == null)
	    	return Set.of();
	    return Set.of(bronknoop, doelknoop);
	}
	
	/**
	 * @post | result == (onderdeel instanceof Leiding l && getDebiet() == l.getDebiet())
	 */
	@Override
	public boolean isGelijkaardigAan(Onderdeel onderdeel) {
		return onderdeel instanceof Leiding l && debiet == l.debiet;
	}
	
}
