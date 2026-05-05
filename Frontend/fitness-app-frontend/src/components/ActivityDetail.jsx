import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router';
import { getActivityDetail } from '../services/api';

const ActivityDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [activity, setActivity] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchActivityDetail = async () => {
      try {
        setLoading(true);
        setError(null);
        const response = await getActivityDetail(id);
        setActivity(response.data);
      } catch (error) {
        console.error(error);
        if (error.response?.status === 404) {
          setError('Recommendations are being generated. Please check back in a moment.');
        } else {
          setError('Failed to load activity details. Please try again.');
        }
      } finally {
        setLoading(false);
      }
    };

    fetchActivityDetail();
  }, [id]);

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
      <div className="flex justify-center items-center py-20">
        <div className="animate-spin rounded-full h-16 w-16 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  if (!activity) {
    return (
      <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-12 text-center">
        <div className="text-6xl mb-4">{error ? '⏳' : '❌'}</div>
        <h3 className="text-xl font-semibold text-gray-900 mb-2">
          {error || 'Activity not found'}
        </h3>
        <p className="text-gray-600 mb-4">
          {error ? 'Our AI is analyzing your activity and generating personalized recommendations.' : ''}
        </p>
        <div className="flex gap-3 justify-center">
          <button
            onClick={() => navigate('/activities')}
            className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
          >
            Back to Activities
          </button>
          {error && (
            <button
              onClick={() => window.location.reload()}
              className="px-6 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 transition-colors"
            >
              Refresh
            </button>
          )}
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      {/* Back Button */}
      <button
        onClick={() => navigate('/activities')}
        className="flex items-center text-gray-600 hover:text-gray-900 transition-colors"
      >
        <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
        </svg>
        Back to Activities
      </button>

      {/* Activity Header Card */}
      <div className="bg-white rounded-2xl shadow-sm border border-gray-200 overflow-hidden">
        <div className={`h-3 bg-gradient-to-r ${getActivityColor(activity.activityType)}`}></div>
        
        <div className="p-8">
          <div className="flex items-start justify-between mb-6">
            <div className="flex items-center space-x-4">
              <div className="text-5xl">{getActivityIcon(activity.activityType)}</div>
              <div>
                <h1 className="text-3xl font-bold text-gray-900">
                  {activity.activityType ? 
                    activity.activityType.charAt(0) + activity.activityType.slice(1).toLowerCase() : 
                    'Activity Details'}
                </h1>
                <p className="text-gray-600 mt-1">
                  {new Date(activity.createdAt).toLocaleString('en-US', {
                    weekday: 'long',
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric',
                    hour: '2-digit',
                    minute: '2-digit'
                  })}
                </p>
              </div>
            </div>
          </div>

          {/* Stats Grid */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="bg-gradient-to-br from-blue-50 to-blue-100 rounded-xl p-6 border border-blue-200">
              <div className="flex items-center justify-between">
                <div>
                  <div className="text-sm text-blue-700 font-medium mb-1">Duration</div>
                  <div className="text-3xl font-bold text-blue-900">{activity.duration}</div>
                  <div className="text-sm text-blue-600 mt-1">minutes</div>
                </div>
                <div className="text-4xl">⏱️</div>
              </div>
            </div>

            <div className="bg-gradient-to-br from-orange-50 to-orange-100 rounded-xl p-6 border border-orange-200">
              <div className="flex items-center justify-between">
                <div>
                  <div className="text-sm text-orange-700 font-medium mb-1">Calories Burned</div>
                  <div className="text-3xl font-bold text-orange-900">{activity.caloriesBurned}</div>
                  <div className="text-sm text-orange-600 mt-1">kcal</div>
                </div>
                <div className="text-4xl">🔥</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* AI Recommendations */}
      {activity.recommendation && (
        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-8">
          <div className="flex items-center space-x-3 mb-6">
            <div className="w-10 h-10 bg-gradient-to-br from-purple-500 to-pink-500 rounded-lg flex items-center justify-center">
              <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
              </svg>
            </div>
            <h2 className="text-2xl font-bold text-gray-900">AI-Powered Insights</h2>
          </div>

          {/* Analysis */}
          <div className="mb-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-3 flex items-center">
              <span className="w-2 h-2 bg-purple-500 rounded-full mr-2"></span>
              Analysis
            </h3>
            <p className="text-gray-700 leading-relaxed bg-gray-50 rounded-lg p-4">
              {activity.recommendation}
            </p>
          </div>

          {/* Improvements */}
          {activity.improvements && activity.improvements.length > 0 && (
            <div className="mb-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-3 flex items-center">
                <span className="w-2 h-2 bg-blue-500 rounded-full mr-2"></span>
                Areas for Improvement
              </h3>
              <div className="space-y-2">
                {activity.improvements.map((improvement, index) => (
                  <div key={index} className="flex items-start bg-blue-50 rounded-lg p-4 border border-blue-100">
                    <svg className="w-5 h-5 text-blue-600 mr-3 mt-0.5 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" />
                    </svg>
                    <p className="text-gray-700">{improvement}</p>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* Suggestions */}
          {activity.suggestions && activity.suggestions.length > 0 && (
            <div className="mb-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-3 flex items-center">
                <span className="w-2 h-2 bg-green-500 rounded-full mr-2"></span>
                Suggestions
              </h3>
              <div className="space-y-2">
                {activity.suggestions.map((suggestion, index) => (
                  <div key={index} className="flex items-start bg-green-50 rounded-lg p-4 border border-green-100">
                    <svg className="w-5 h-5 text-green-600 mr-3 mt-0.5 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                    <p className="text-gray-700">{suggestion}</p>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* Safety Guidelines */}
          {activity.safety && activity.safety.length > 0 && (
            <div>
              <h3 className="text-lg font-semibold text-gray-900 mb-3 flex items-center">
                <span className="w-2 h-2 bg-red-500 rounded-full mr-2"></span>
                Safety Guidelines
              </h3>
              <div className="space-y-2">
                {activity.safety.map((safety, index) => (
                  <div key={index} className="flex items-start bg-red-50 rounded-lg p-4 border border-red-100">
                    <svg className="w-5 h-5 text-red-600 mr-3 mt-0.5 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
                    </svg>
                    <p className="text-gray-700">{safety}</p>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default ActivityDetail;
