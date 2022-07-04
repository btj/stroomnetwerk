package stroomnetwerk;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @invar | getInkomendeLeidingen() != null
 * @invar | getUitgaandeLeidingen() != null
 * @invar | getInkomendeLeidingen().stream().allMatch(l -> l != null && l.getDoelknoop() == this)
 * @invar | getUitgaandeLeidingen().stream().allMatch(l -> l != null && l.getBronknoop() == this)
 */
public class Knoop extends Onderdeel {
	
	/**
	 * @invar | inkomendeLeidingen != null
	 * @invar | uitgaandeLeidingen != null
	 * @invar | inkomendeLeidingen.stream().allMatch(l -> l != null && l.doelknoop == this)
	 * @invar | uitgaandeLeidingen.stream().allMatch(l -> l != null && l.bronknoop == this)
	 * 
	 * @representationObject
	 * @peerObjects
	 */
	Set<Leiding> inkomendeLeidingen = new HashSet<>();
	/**
	 * @representationObject
	 * @peerObjects
	 */
	Set<Leiding> uitgaandeLeidingen = new HashSet<>();
	
	/**
	 * @creates | result
	 * @peerObjects
	 */
	public Set<Leiding> getInkomendeLeidingen() { return Set.copyOf(inkomendeLeidingen); }
	
	/**
	 * @creates | result
	 * @peerObjects
	 */
	public Set<Leiding> getUitgaandeLeidingen() { return Set.copyOf(uitgaandeLeidingen); }

	/**
	 * @post | getInkomendeLeidingen().isEmpty()
	 * @post | getUitgaandeLeidingen().isEmpty()
	 */
	public Knoop() {}
	
	/**
	 * @creates | result
	 * @post | result != null
	 * @post | getInkomendeLeidingen().stream().allMatch(l -> result.contains(l))
	 * @post | getUitgaandeLeidingen().stream().allMatch(l -> result.contains(l))
	 * @post | result.stream().allMatch(o -> getInkomendeLeidingen().contains(o) || getUitgaandeLeidingen().contains(o))
	 * @post This is a not a basic inspector.
	 *       | result == result
	 */
	@Override
	public Set<? extends Onderdeel> getGekoppeldeOnderdelen() {
		Set<Leiding> result = new HashSet<>();
		result.addAll(inkomendeLeidingen);
		result.addAll(uitgaandeLeidingen);
		return result;
	}
	
	/**
	 * @post | result == (
	 *       |     onderdeel instanceof Knoop k &&
	 *       |     getInkomendeLeidingen().size() == k.getInkomendeLeidingen().size() &&
	 *       |     getUitgaandeLeidingen().size() == k.getUitgaandeLeidingen().size()
	 *       | )
	 */
	@Override
	public boolean isGelijkaardigAan(Onderdeel onderdeel) {
		return onderdeel instanceof Knoop k &&
				inkomendeLeidingen.size() == k.inkomendeLeidingen.size() &&
				uitgaandeLeidingen.size() == k.uitgaandeLeidingen.size();
	}
	
	public Iterator<Knoop> getOnmiddellijkStroomafwaartseKnopenIterator() {
		return new Iterator<>() {
			Iterator<Leiding> uitgaandeLeidingenIterator = uitgaandeLeidingen.iterator();
			@Override
			public boolean hasNext() {
				return uitgaandeLeidingenIterator.hasNext();
			}
			@Override
			public Knoop next() {
				return uitgaandeLeidingenIterator.next().doelknoop;
			}
		};
	}
	
	public void forEachTegenoverliggendeKnoop(Consumer<? super Knoop> consumer) {
		for (Leiding l : inkomendeLeidingen)
			consumer.accept(l.bronknoop);
		for (Leiding l : uitgaandeLeidingen)
			consumer.accept(l.doelknoop);
	}
	
	public Stream<Knoop> getStroomafwaartseKnopenStream() {
		return uitgaandeLeidingen.stream().map(l -> l.doelknoop).flatMap(k -> Stream.concat(Stream.of(k), k.getStroomafwaartseKnopenStream()));
	}
	
}
