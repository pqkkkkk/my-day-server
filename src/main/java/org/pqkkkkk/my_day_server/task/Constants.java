package org.pqkkkkk.my_day_server.task;

public class Constants {
    public enum ListCategory {
        WORK ("Công việc"),
        STUDY("Việc học"),
        PERSONAL ("Cá nhân"),
        OTHER ("Khác");

        private String description;

        private ListCategory(String description) {
            this.description = description;
        }
        public String getDescription() {
            return description;
        }
    }
    public enum TaskStatus {
        TODO ("Đang chờ"),
        IN_PROGRESS ("Đang thực hiện"),
        DONE ("Đã hoàn thành");

        private String description;

        private TaskStatus(String description) {
            this.description = description;
        }
        public String getDescription() {
            return description;
        }
    }
    public enum TaskPriority {
        LOW ("Thấp"),
        MEDIUM ("Trung bình"),
        HIGH ("Cao");

        private String description;

        private TaskPriority(String description) {
            this.description = description;
        }
        public String getDescription() {
            return description;
        }
    }
    public enum SortDirection{
        ASC ("Tăng dần"),
        DESC ("Giảm dần");

        private String description;

        private SortDirection(String description) {
            this.description = description;
        }
        public String getDescription() {
            return description;
        }
    }
}
