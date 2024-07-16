import TaskManager.Type.Epic;
import TaskManager.Type.SubTask;
import TaskManager.Type.Task;
import TaskManager.Type.TaskStatus;

import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {

    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, ArrayList<SubTask>> subTasks = new HashMap<>();

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void searchTask(Task task) {
        System.out.println(tasks.get(task.getId()).getName());
        System.out.println(tasks.get(task.getId()).getDescription());
        System.out.println(tasks.get(task.getId()).getStatus());
    }

    public void getAllTasks() {
        ArrayList<Task> task = new ArrayList<>(tasks.values());
        for (Task value : task) {
            System.out.println(value.getName());
        }
    }

    public void clearTaskList() {
        tasks.clear();
    }

    public void removeTask(Task task) {
        tasks.remove(task.getId());
    }

    public void updateTask(Task oldTask, Task updateTask) {
        for (Integer task : tasks.keySet()) {
            if (task == oldTask.getId()) {
                tasks.put(oldTask.getId(), updateTask);
            }
        }
    }

    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        subTasks.put(epic.getId(), new ArrayList<>());
    }

    public ArrayList<String> getAllEpics() {
        ArrayList<String> epicsList = new ArrayList<>();
        for (Epic epic : epics.values()) {
            epicsList.add(epic.getName());
        }

        return epicsList;
    }

    public void searchEpic(Epic epic) {
        for (Integer epicID : epics.keySet()) {
            if (epicID == epic.getId()) {
                checkStatus(epic);
                System.out.println(epics.get(epicID).getName());
                System.out.println(epics.get(epicID).getDescription());
                System.out.println(epics.get(epicID).getStatus());
            }
        }
    }

    public void removeEpic(Epic epic) {
        epics.remove(epic.getId());
        subTasks.remove(epic.getId());
    }

    public void clearEpics() {
        epics.clear();
        subTasks.clear();
    }

    private void checkStatus(Epic epic) {
        ArrayList<TaskStatus> status = new ArrayList<>();
        for (Integer id : subTasks.keySet()) {
            if (id == epic.getId()) {
                for (int i = 0; i < subTasks.get(epic.getId()).size(); i++) {
                    status.add(subTasks.get(epic.getId()).get(i).getStatus());
                }
            }
        }
        ArrayList<TaskStatus> statusNew = new ArrayList<>();
        ArrayList<TaskStatus> statusInProgress = new ArrayList<>();
        ArrayList<TaskStatus> statusDone = new ArrayList<>();
        for (int i = 0; i < status.size(); i++) {
            if (status.get(i).equals(TaskStatus.DONE)) {
                statusDone.add(TaskStatus.DONE);
            } else if (status.get(i).equals(TaskStatus.IN_PROGRESS)) {
                statusInProgress.add(TaskStatus.IN_PROGRESS);
            } else if (status.get(i).equals(TaskStatus.NEW)) {
                statusNew.add(TaskStatus.NEW);
            }
        }
        if (!statusInProgress.isEmpty() && !statusNew.isEmpty() || (statusInProgress.isEmpty() && !statusNew.isEmpty())) {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        } else if (!statusDone.isEmpty() && statusInProgress.isEmpty() && statusNew.isEmpty()) {
            epic.setStatus(TaskStatus.DONE);
        }
    }

    public void addSubTaskToEpic(Epic epic, SubTask subTask) {
        for (Integer id : subTasks.keySet()) {
            if (id == epic.getId()) {
                subTask.setId(epic.getId());
                subTask.setEpicId(epic.getId());
                subTasks.get(epic.getId()).add(subTask);
            }
        }
    }

    public ArrayList<String> getAllSubTasksEpic(Epic epic) {
        ArrayList<String> subTasksList = new ArrayList<>();
        for (Integer idEpic : subTasks.keySet()) {
            if (idEpic == epic.getId()) {
                for (int i = 0; i < subTasks.get(epic.getId()).size(); i++) {
                    subTasksList.add(subTasks.get(epic.getId()).get(i).getName());
                }
            }
        }
        return subTasksList;
    }

    public void searchSubTaskID(SubTask subTask) {
        for (ArrayList<SubTask> subTasks : subTasks.values()) {
            for (SubTask sub : subTasks) {
                if (sub.getId() == subTask.getId()) {
                    System.out.println(sub.getName());
                    System.out.println(sub.getDescription());
                    System.out.println(sub.getStatus());
                }
            }
        }
    }

    public void removeSubTask(SubTask subTask) {
        for (ArrayList<SubTask> idSub : subTasks.values()) {
            idSub.remove(subTask);
        }
    }

    public void clearSubTask(Epic epic) {
        subTasks.put(epic.getId(), new ArrayList<>());
    }

    public void updateSab(SubTask oldSub, SubTask newSub) {
        SubTask subTaskNEW = new SubTask();
        for (ArrayList<SubTask> subList : subTasks.values()) {
            for (SubTask subTask : subList) {
                if (oldSub.getId() == subTask.getId()) {
                    subTaskNEW.setId(oldSub.getId());
                    subTaskNEW.setEpicId(oldSub.getEpicId());
                    subTaskNEW.setStatus(TaskStatus.DONE);
                    subTaskNEW.setName(newSub.getName());
                    subTaskNEW.setDescription(newSub.getDescription());
                }
            }
        }
        removeSubTask(oldSub);
        subTasks.get(subTaskNEW.getEpicId()).add(subTaskNEW);
    }
}