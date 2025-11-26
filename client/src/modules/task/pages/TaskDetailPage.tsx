import { useParams } from "react-router-dom";

const TaskDetailPage = () => {
  const { id } = useParams<{ id: string }>();

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold text-gray-900 mb-4">Task Details</h1>
      <p className="text-gray-600">Task ID: {id}</p>
      <p className="text-gray-600">Task details will be implemented later</p>
    </div>
  );
};

export default TaskDetailPage;

