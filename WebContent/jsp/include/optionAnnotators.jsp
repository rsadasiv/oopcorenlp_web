<%
String selectedAnnotator = request.getParameter("Annotation")==null?(request.getAttribute("Annotation")==null?"":request.getAttribute("Annotation").toString()):request.getParameter("Annotation");
for (String annotator : (java.util.List<String>) request.getAttribute("Annotators")) {
%>
<option value="<%=annotator %>" <%=annotator.equals(selectedAnnotator)?"selected":"" %>><%=annotator %></option>
<%
}
%>