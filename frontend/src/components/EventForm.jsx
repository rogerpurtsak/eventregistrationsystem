import { useState } from 'react';

export default function EventForm({ onSubmit }) {
  const [title, setTitle] = useState('');
  const [eventTime, setEventTime] = useState('');
  const [maxParticipants, setMaxParticipants] = useState('');
  const [error, setError] = useState('');

  async function handleSubmit(e) {
    e.preventDefault();
    setError('');
    try {
      await onSubmit({ title, eventTime, maxParticipants: parseInt(maxParticipants) });
      setTitle('');
      setEventTime('');
      setMaxParticipants('');
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <form onSubmit={handleSubmit}>
      <h2>Create Event</h2>
      <input
        type="text"
        placeholder="Title"
        value={title}
        onChange={e => setTitle(e.target.value)}
      />
      <input
        type="datetime-local"
        value={eventTime}
        onChange={e => setEventTime(e.target.value)}
      />
      <input
        type="number"
        placeholder="Max participants"
        value={maxParticipants}
        onChange={e => setMaxParticipants(e.target.value)}
      />
      {error && <p>{error}</p>}
      <button type="submit">Create</button>
    </form>
  );
}
