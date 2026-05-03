import { useState } from 'react';

export default function EventForm({ onSubmit, onCancel }) {
  const [title, setTitle] = useState('');
  const [eventTime, setEventTime] = useState('');
  const [maxParticipants, setMaxParticipants] = useState('');
  const [error, setError] = useState('');

  function validate() {
    if (!title.trim()) return 'Title is required.';
    if (!eventTime) return 'Event time is required.';
    if (!maxParticipants || parseInt(maxParticipants) < 1) return 'Max participants must be at least 1.';
    return null;
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError('');
    const validationError = validate();
    if (validationError) {
      setError(validationError);
      return;
    }
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
      {onCancel && <button type="button" onClick={onCancel}>Cancel</button>}
    </form>
  );
}
