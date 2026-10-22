package com.example.studentmanagement;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StudentController {
    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    private String pageStart(String title) {
        return """
            <!DOCTYPE html>
            <html lang="en"><head><meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <title>__PAGE_TITLE__ | EduNova</title>
            <style>
              :root{--navy:#102747;--blue:#2457d6;--teal:#19b6a4;--light:#f3f7fc;--muted:#69788e}
              *{box-sizing:border-box}body{margin:0;font-family:Segoe UI,Arial,sans-serif;background:var(--light);color:#24334b}
              a{text-decoration:none}.nav{background:#fff;padding:18px 6%;display:flex;align-items:center;justify-content:space-between;gap:18px;box-shadow:0 3px 18px #10274710;flex-wrap:wrap}
              .logo{font-size:24px;font-weight:800;color:var(--navy)}.logo span{color:var(--teal)}.navlinks{display:flex;gap:18px;flex-wrap:wrap}.navlinks a{color:var(--blue);font-weight:700}
              .hero{background:linear-gradient(120deg,#102747,#244c83);padding:34px 6%;color:white}.hero h1{margin:0 0 8px}.hero p{margin:0;color:#dbe7f7}
              .wrap{width:min(100% - 32px,1150px);margin:32px auto}.card{padding:26px;background:white;border-radius:16px;box-shadow:0 12px 35px #1027470b;border:1px solid #e6edf6;margin-bottom:22px}
              .grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(190px,1fr));gap:18px}.metric{padding:22px;background:white;border:1px solid #e6edf6;border-radius:14px}.metric .number{font-size:30px;font-weight:800;color:var(--blue);margin-top:8px}.muted{color:var(--muted);line-height:1.7}
              table{width:100%;border-collapse:collapse;min-width:650px}th,td{text-align:left;padding:13px 14px;border-bottom:1px solid #e6edf6}th{background:var(--navy);color:white}tr:hover td{background:#f7faff}.table-wrap{overflow-x:auto;border-radius:10px}
              .field{margin:16px 0}label{display:block;margin-bottom:7px;font-weight:700}input,select{width:100%;padding:12px;border:1px solid #dce4ef;border-radius:9px;font:inherit;background:#fbfdff}
              .btn{display:inline-block;padding:12px 18px;border-radius:9px;border:0;font-weight:700;cursor:pointer}.primary{background:var(--blue);color:white}.secondary{border:1px solid #dce4ef;color:var(--navy);background:white}.full{width:100%}
              .layout{display:grid;grid-template-columns:.85fr 1.15fr;gap:22px;align-items:start}.footer{text-align:center;padding:24px;color:var(--muted);font-size:13px}
              @media(max-width:700px){.layout{grid-template-columns:1fr}.hero h1{font-size:26px}.card{padding:18px}}
            </style></head><body>
            <header class="nav"><a class="logo" href="/">Edu<span>Nova</span> 🎓</a>
            <nav class="navlinks"><a href="/">Home</a><a href="/students/register">Register</a><a href="/admin">Admin Panel</a></nav></header>
            """.replace("__PAGE_TITLE__", escapeHtml(title));
    }

    @GetMapping("/students/register")
    @ResponseBody
    public String showRegistrationForm() {
        return pageStart("Student Registration") + """
            <section class="hero"><h1>Student Registration</h1><p>Register for a course at EduNova.</p></section>
            <main class="wrap"><div class="layout">
              <section class="card"><h2>Join EduNova</h2><p class="muted">Enter the student's details. After submission, the record is saved in the student database.</p><p>✓ Course selection</p><p>✓ Database-backed registration</p><p>✓ Confirmation with student ID</p></section>
              <form class="card" action="/students/register" method="post"><h2>Your details</h2>
                <div class="field"><label for="name">Full name *</label><input id="name" name="name" required maxlength="100" placeholder="Enter full name"></div>
                <div class="field"><label for="email">Email address *</label><input id="email" name="email" type="email" required maxlength="150" placeholder="you@example.com"></div>
                <div class="field"><label for="course">Course *</label><select id="course" name="course" required><option value="">Choose course</option><option>MCA</option><option>BCA</option><option>BBA</option><option>B.Com</option><option>Computer Science</option><option>Web Development</option><option>Data Analytics</option><option>AI &amp; Machine Learning</option><option>Other</option></select></div>
                <div class="field"><label for="phone">Phone number *</label><input id="phone" name="phone" type="tel" required maxlength="20" pattern="[0-9+() -]{7,20}" placeholder="Enter phone number"></div>
                <button class="btn primary full" type="submit">Submit Registration</button>
              </form></div></main><footer class="footer">© EduNova · Learn. Grow. Achieve.</footer></body></html>
            """;
    }

    @PostMapping("/students/register")
    @ResponseBody
    public String registerStudent(@RequestParam String name, @RequestParam String email,
                                  @RequestParam String course, @RequestParam String phone) {
        Student student = new Student(name.trim(), email.trim(), course.trim(), phone.trim());
        Student saved = studentRepository.save(student);
        return pageStart("Registration Successful") + """
            <main class="wrap"><section class="card" style="max-width:700px;margin:30px auto;text-align:center">
            <div style="font-size:48px;color:#087d58">✓</div><h1>Registration Successful!</h1>
            <p class="muted">The student record has been saved successfully.</p>
            <p>Your Student ID</p><h2 style="color:#2457d6"># __ID__</h2>
            <div style="text-align:left;line-height:2"><b>Full name:</b> __NAME__<br><b>Email:</b> __EMAIL__<br><b>Course:</b> __COURSE__<br><b>Phone:</b> __PHONE__</div>
            <p style="margin-top:24px"><a class="btn primary" href="/">Back to Home</a> <a class="btn secondary" href="/students/register">Register Another Student</a> <a class="btn secondary" href="/admin">Admin Panel</a></p>
            </section></main><footer class="footer">© EduNova College Portal · Learn. Grow. Achieve.</footer></body></html>
            """.replace("__ID__", String.valueOf(saved.getId()))
               .replace("__NAME__", escapeHtml(saved.getName()))
               .replace("__EMAIL__", escapeHtml(saved.getEmail()))
               .replace("__COURSE__", escapeHtml(saved.getCourse()))
               .replace("__PHONE__", escapeHtml(saved.getPhone()));
    }

    @GetMapping("/students")
    @ResponseBody
    public String viewStudents() {
        List<Student> students = studentRepository.findAll();
        StringBuilder html = new StringBuilder(pageStart("Registered Students"));
        html.append("<section class=\"hero\"><h1>Registered Students</h1><p>All student records saved in EduNova.</p></section>")
            .append("<main class=\"wrap\"><section class=\"card\"><h2>Student records (")
            .append(students.size()).append(")</h2><div class=\"table-wrap\"><table><thead><tr><th>ID</th><th>Name</th><th>Email</th><th>Course</th><th>Phone</th></tr></thead><tbody>");
        if (students.isEmpty()) html.append("<tr><td colspan=\"5\">No students registered yet.</td></tr>");
        for (Student s : students) {
            html.append("<tr><td>").append(s.getId()).append("</td><td>").append(escapeHtml(s.getName()))
                .append("</td><td>").append(escapeHtml(s.getEmail())).append("</td><td>")
                .append(escapeHtml(s.getCourse())).append("</td><td>").append(escapeHtml(s.getPhone())).append("</td></tr>");
        }
        return html.append("</tbody></table></div><p style=\"margin-top:22px\"><a class=\"btn primary\" href=\"/admin\">Admin Dashboard</a></p></section></main><footer class=\"footer\">© EduNova</footer></body></html>").toString();
    }

    @GetMapping("/admin")
    @ResponseBody
    public String adminDashboard() {
        List<Student> students = studentRepository.findAll();
        Map<String, Long> courseCounts = students.stream().collect(Collectors.groupingBy(
            s -> s.getCourse() == null || s.getCourse().isBlank() ? "Not specified" : s.getCourse().trim(),
            Collectors.counting()));
        StringBuilder html = new StringBuilder(pageStart("Admin Dashboard"));
        html.append("<section class=\"hero\"><h1>Admin Dashboard</h1><p>EduNova student registration overview</p></section><main class=\"wrap\">")
            .append("<div class=\"grid\"><div class=\"metric\"><div class=\"muted\">Total registered students</div><div class=\"number\">").append(students.size()).append("</div></div>")
            .append("<div class=\"metric\"><div class=\"muted\">Courses selected</div><div class=\"number\">").append(courseCounts.size()).append("</div></div>")
            .append("<div class=\"metric\"><div class=\"muted\">Active users / logins</div><div class=\"number\">Not tracked</div><p class=\"muted\">A login and session system is required for this metric.</p></div></div>")
            .append("<section class=\"card\" style=\"margin-top:22px\"><h2>Students by course</h2><div class=\"table-wrap\"><table><thead><tr><th>Course</th><th>Registered students</th></tr></thead><tbody>");
        if (courseCounts.isEmpty()) html.append("<tr><td colspan=\"2\">No course registrations yet.</td></tr>");
        courseCounts.entrySet().stream().sorted(Map.Entry.comparingByKey(String.CASE_INSENSITIVE_ORDER)).forEach(e ->
            html.append("<tr><td>").append(escapeHtml(e.getKey())).append("</td><td>").append(e.getValue()).append("</td></tr>"));
        html.append("</tbody></table></div></section><section class=\"card\"><h2>All registered students</h2><div class=\"table-wrap\"><table><thead><tr><th>ID</th><th>Name</th><th>Email</th><th>Course</th><th>Phone</th></tr></thead><tbody>");
        if (students.isEmpty()) html.append("<tr><td colspan=\"5\">No students registered yet.</td></tr>");
        for (Student s : students) html.append("<tr><td>").append(s.getId()).append("</td><td>").append(escapeHtml(s.getName()))
            .append("</td><td>").append(escapeHtml(s.getEmail())).append("</td><td>").append(escapeHtml(s.getCourse()))
            .append("</td><td>").append(escapeHtml(s.getPhone())).append("</td></tr>");
        html.append("</tbody></table></div><p style=\"margin-top:20px\"><a class=\"btn primary\" href=\"/students/register\">Register Student</a> <a class=\"btn secondary\" href=\"/students\">Open Students Page</a></p></section></main><footer class=\"footer\">© EduNova Admin</footer></body></html>");
        return html.toString();
    }

    private String escapeHtml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                    .replace("\"", "&quot;").replace("'", "&#39;");
    }
}
