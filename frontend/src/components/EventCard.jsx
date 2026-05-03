export default function EventCard({ event, onRegister }) {
  const isFull = event.availableSpots <= 0;

  return (
    <div className="event-card">
      <h3>{event.title}</h3>
      <p>Time: {new Date(event.eventTime).toLocaleString()}</p>
      <p>Registered: {event.registeredCount} / {event.maxParticipants}</p>
      <p>Available spots: {event.availableSpots}</p>
      {isFull ? (
        <p className="event-full">Event is full</p>
      ) : (
        <button onClick={() => onRegister(event)}>Register</button>
      )}
    </div>
  );
}
