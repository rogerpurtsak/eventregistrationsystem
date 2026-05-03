export default function EventCard({ event, onRegister }) {
  return (
    <div>
      <h3>{event.title}</h3>
      <p>Time: {new Date(event.eventTime).toLocaleString()}</p>
      <p>Spots: {event.availableSpots} / {event.maxParticipants}</p>
      <button onClick={() => onRegister(event)}>Register</button>
    </div>
  );
}
