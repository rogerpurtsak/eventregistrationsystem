import EventCard from './EventCard';

export default function EventList({ events, onRegister }) {
  if (events.length === 0) {
    return <p>No events have been added yet.</p>;
  }

  return (
    <div>
      {events.map(event => (
        <EventCard key={event.id} event={event} onRegister={onRegister} />
      ))}
    </div>
  );
}
