<%--
  Created by IntelliJ IDEA.
  User: gatesma
  Date: 2019/12/4
  Time: 18:13
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="GB2312" %>
<!DOCTYPE html>
<html>
<head>
    <title>��ɱ����</title>
    <%@include file="common/head.jsp" %>
</head>
<body>

<div class="container">
    <div class="panel panel-default text-center">
        <div class="panel-heading">
            <h1>${seckill.name}</h1>
        </div>
        <div class="panel-body">
            <h2 class="text-danger">
                <%--��ʾtimeͼ��--%>
                <span class="glyphicon glyphicon-time"></span>
                <%--չʾ����ʱ--%>
                <span class="glyphicon" id="seckill-box"></span>
            </h2>
        </div>
    </div>
</div>

<div id="killPhoneModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title text-center">
                    <span class="glyphicon glyphicon-phone"></span>
                </h3>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-8 col-xs-offset-2">
                        <input type="text" name="killPhone" id="killPhoneKey"
                               placeholder="���ֻ���" class="form-control"/>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <!--��֤��Ϣ-->
                <span id="killPhoneMessage" class="glyphicon"></span>
                <button type="button" id="killPhoneBtn" class="btn btn-success">
                    <span class="glyphicon glyphicon-phone"></span>
                    Submit
                </button>
            </div>
        </div>
    </div>
</div>


</body>
<!-- jQuery�ļ��������bootstrap.min.js ֮ǰ���� -->
<script src="https://cdn.staticfile.org/jquery/2.1.1/jquery.min.js"></script>
<!-- ���µ� Bootstrap ���� JavaScript �ļ� -->
<script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>

<!-- ʹ��CDN��ȡ����js �� http://www.bootcdn.cn/ -->
<!-- jQuery cookie������� -->
<script src="https://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.js"></script>
<!-- jQuery countDown����ʱ��� -->
<script src="https://cdn.bootcss.com/jquery.countdown/2.2.0/jquery.countdown.js"></script>


<!-- ��ʼ��д�����߼� -->
<script src="/seckill/resources/script/seckill.js" type="text/javascript"></script>
<script type="text/javascript">
    $(function () {

        // ʹ��EL���ʽ�������
        seckill.detail.init({
            seckillId: ${seckill.seckillId},
            startTime: ${seckill.startTime.time}, // ����ֱ��ʹ��.time�� EL���ʽ���Զ��ĵ���getTime����
            endTime: ${seckill.endTime.time} // ����ʱ��
        });
    })
</script>
</html>