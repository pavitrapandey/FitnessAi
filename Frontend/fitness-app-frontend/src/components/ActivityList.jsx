import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import { getActivities, deleteActivity } from '../services/api';

const ActivityList = () => {
  const [activities, setActivities] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const fetchActivities = async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await getActivities();
      setActivities(response.data);
    } catch (error) {
      console.error('Error fetching activities:', error);
      setError(error.response?.data?.message || error.message || 'Failed to load activities');
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteActivity = async (activityId, event) => {
    event.stopPropagation(); // Prevent navigation when clicking delete
    if (window.confirm('Are you sure you want to delete this activity?')) {
      try {
        await deleteActivity(activityId);
        // Refresh the activities list
        fetchActivities();
      } catch (error) {
        console.error('Error deleting activity:', error);
        alert('Failed to delete activity. Please try again.');
      }
    }
  };

  useEffect(() => {
    fetchActivities();
  }, []);

  const getActivityIcon = (type) => {
    const icons = {
      RUNNING: '🏃',
      WALKING: '🚶',
      CYCLING: '🚴',
      SWIMMING: '🏊',
      GYM: '🏋️',
      HIIT: '💪',
      STRETCHING: '🧘',
      YOGA: '🧘‍♀️',
      PILATES: '🤸',
      BOXING: '🥊',
      HIKING: '🥾',
      DANCING: '💃',
      ROWING: '🚣',
      CROSSFIT: '⚡',
      MARTIAL_ARTS: '🥋',
      AEROBICS: '🤾',
      CLIMBING: '🧗',
      SKIPPING: '🪢',
      MEDITATION: '🧘‍♂️'
    };
    return icons[type] || '💪';
  };

  const getActivityColor = (type) => {
    const colors = {
      RUNNING: 'from-red-500 to-orange-500',
      WALKING: 'from-green-500 to-teal-500',
      CYCLING: 'from-blue-500 to-cyan-500',
      SWIMMING: 'from-cyan-500 to-blue-500',
      GYM: 'from-gray-600 to-gray-800',
      HIIT: 'from-orange-500 to-red-600',
      STRETCHING: 'from-purple-400 to-pink-400',
      YOGA: 'from-indigo-400 to-purple-500',
      PILATES: 'from-pink-400 to-rose-500',
      BOXING: 'from-red-600 to-orange-600',
      HIKING: 'from-green-600 to-emerald-600',
      DANCING: 'from-fuchsia-500 to-pink-500',
      ROWING: 'from-blue-600 to-indigo-600',
      CROSSFIT: 'from-yellow-500 to-orange-600',
      MARTIAL_ARTS: 'from-slate-700 to-gray-900',
      AEROBICS: 'from-lime-500 to-green-500',
      CLIMBING: 'from-amber-600 to-orange-700',
      SKIPPING: 'from-violet-500 to-purple-600',
      MEDITATION: 'from-sky-400 to-indigo-500'
    };
    return colors[type] || 'from-purple-500 to-pink-500';
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center py-12">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="bg-red-50 border border-red-200 rounded-2xl p-8 text-center">
        <div className="text-6xl mb-4">⚠️</div>
        <h3 className="text-xl font-semibold text-red-900 mb-2">Error Loading Activities</h3>
        <p className="text-red-700 mb-4">{error}</p>
        <button
          onClick={fetchActivities}
          className="px-6 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors"
        >
          Try Again
        </button>
      </div>
    );
  }

  if (activities.length === 0) {
    return (
      <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-12 text-center">
        <div className="text-6xl mb-4">🏋️</div>
        <h3 className="text-xl font-semibold text-gray-900 mb-2">No activities yet</h3>
        <p className="text-gray-600">Start tracking your fitness journey by adding your first activity above!</p>
      </div>
    );
  }

  return (
    <div>
      <div className="mb-6">
        <h2 className="text-2xl font-bold text-gray-900">Your Activities</h2>
        <p className="text-gray-600 mt-1">Click on any activity to view detailed insights</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {activities.map((activity) => (
          <div
            key={activity.id}
            onClick={() => navigate(`/activities/${activity.id}`)}
            className="bg-white rounded-xl shadow-sm border border-gray-200 hover:shadow-lg hover:border-blue-300 transition-all duration-200 cursor-pointer overflow-hidden group"
          >
            {/* Gradient Header */}
            <div className={`h-2 bg-gradient-to-r ${getActivityColor(activity.type)}`}></div>
            
            <div className="p-5">
              {/* Activity Type */}
              <div className="flex items-center justify-between mb-4">
                <div className="flex items-center space-x-3">
                  <div className="text-3xl">{getActivityIcon(activity.type)}</div>
                  <div>
                    <h3 className="text-lg font-semibold text-gray-900 group-hover:text-blue-600 transition-colors">
                      {activity.type.charAt(0) + activity.type.slice(1).toLowerCase()}
                    </h3>
                    <p className="text-xs text-gray-500">
                      {new Date(activity.createdAt).toLocaleDateString()}
                    </p>
                  </div>
                </div>
              </div>

              {/* Stats */}
              <div className="grid grid-cols-2 gap-4">
                <div className="bg-gray-50 rounded-lg p-3">
                  <div className="text-xs text-gray-600 mb-1">Duration</div>
                  <div className="text-lg font-bold text-gray-900">{activity.duration}</div>
                  <div className="text-xs text-gray-500">minutes</div>
                </div>
                <div className="bg-gray-50 rounded-lg p-3">
                  <div className="text-xs text-gray-600 mb-1">Calories</div>
                  <div className="text-lg font-bold text-gray-900">{activity.caloriesBurned}</div>
                  <div className="text-xs text-gray-500">kcal</div>
                </div>
              </div>

              {/* View Details Arrow */}
              <div className="mt-4 flex items-center justify-between">
                <div className="flex items-center text-sm text-blue-600 font-medium group-hover:translate-x-1 transition-transform">
                  View details
                  <svg className="w-4 h-4 ml-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                  </svg>
                </div>
                <button
                  onClick={(e) => handleDeleteActivity(activity.id, e)}
                  className="p-2 text-red-600 hover:bg-red-50 rounded-lg transition-colors"
                  title="Delete activity"
                >
                  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                  </svg>
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ActivityList;
