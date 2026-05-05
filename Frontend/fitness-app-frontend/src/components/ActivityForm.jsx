import React, { useState } from 'react';
import { addActivity } from '../services/api';

const ActivityForm = ({ onActivityAdded }) => {
  const [activity, setActivity] = useState({
    type: "RUNNING",
    duration: '',
    caloriesBurned: '',
    additionalMetrics: {}
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await addActivity(activity);
      onActivityAdded();
      setActivity({ type: "RUNNING", duration: '', caloriesBurned: '', additionalMetrics: {} });
    } catch (error) {
      console.error(error);
    }
  };

  const activityTypes = [
    { value: "RUNNING", label: "Running", icon: "🏃" },
    { value: "WALKING", label: "Walking", icon: "🚶" },
    { value: "CYCLING", label: "Cycling", icon: "🚴" },
    { value: "SWIMMING", label: "Swimming", icon: "🏊" },
    { value: "GYM", label: "Gym", icon: "🏋️" },
    { value: "HIIT", label: "HIIT", icon: "💪" },
    { value: "STRETCHING", label: "Stretching", icon: "🧘" },
    { value: "YOGA", label: "Yoga", icon: "🧘‍♀️" },
    { value: "PILATES", label: "Pilates", icon: "🤸" },
    { value: "BOXING", label: "Boxing", icon: "🥊" },
    { value: "HIKING", label: "Hiking", icon: "🥾" },
    { value: "DANCING", label: "Dancing", icon: "💃" },
    { value: "ROWING", label: "Rowing", icon: "🚣" },
    { value: "CROSSFIT", label: "CrossFit", icon: "⚡" },
    { value: "MARTIAL_ARTS", label: "Martial Arts", icon: "🥋" },
    { value: "AEROBICS", label: "Aerobics", icon: "🤾" },
    { value: "CLIMBING", label: "Climbing", icon: "🧗" },
    { value: "SKIPPING", label: "Skipping", icon: "🪢" },
    { value: "MEDITATION", label: "Meditation", icon: "🧘‍♂️" }
  ];

  return (
    <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-6">
      <div className="mb-6">
        <h2 className="text-2xl font-bold text-gray-900">Add New Activity</h2>
        <p className="text-gray-600 mt-1">Track your workout and get AI-powered insights</p>
      </div>

      <form onSubmit={handleSubmit} className="space-y-5">
        {/* Activity Type Selection */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-3">
            Activity Type
          </label>
          <div className="grid grid-cols-3 sm:grid-cols-4 md:grid-cols-5 lg:grid-cols-6 gap-3">
            {activityTypes.map((type) => (
              <button
                key={type.value}
                type="button"
                onClick={() => setActivity({ ...activity, type: type.value })}
                className={`p-4 rounded-xl border-2 transition-all duration-200 ${
                  activity.type === type.value
                    ? 'border-blue-500 bg-blue-50 shadow-md'
                    : 'border-gray-200 hover:border-gray-300 hover:bg-gray-50'
                }`}
              >
                <div className="text-3xl mb-2">{type.icon}</div>
                <div className={`text-sm font-medium ${
                  activity.type === type.value ? 'text-blue-700' : 'text-gray-700'
                }`}>
                  {type.label}
                </div>
              </button>
            ))}
          </div>
        </div>

        {/* Duration Input */}
        <div>
          <label htmlFor="duration" className="block text-sm font-medium text-gray-700 mb-2">
            Duration (minutes)
          </label>
          <input
            id="duration"
            type="number"
            required
            min="1"
            value={activity.duration}
            onChange={(e) => setActivity({ ...activity, duration: e.target.value })}
            className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200"
            placeholder="Enter duration in minutes"
          />
        </div>

        {/* Calories Input */}
        <div>
          <label htmlFor="calories" className="block text-sm font-medium text-gray-700 mb-2">
            Calories Burned
          </label>
          <input
            id="calories"
            type="number"
            required
            min="1"
            value={activity.caloriesBurned}
            onChange={(e) => setActivity({ ...activity, caloriesBurned: e.target.value })}
            className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200"
            placeholder="Enter calories burned"
          />
        </div>

        {/* Submit Button */}
        <button
          type="submit"
          className="w-full bg-gradient-to-r from-blue-600 to-purple-600 text-white font-semibold py-3 px-6 rounded-lg hover:from-blue-700 hover:to-purple-700 transform hover:scale-[1.02] transition-all duration-200 shadow-md hover:shadow-lg"
        >
          Add Activity
        </button>
      </form>
    </div>
  );
};

export default ActivityForm;
