// 存放主要交互逻辑代码
// javascript 模块化

// seckill.detail.init(params)
var seckill = {
    //封装秒杀相关ajax的地址
    URL: {
        now: function () {
            return '/seckill/seckill/time/now';
        },
        exposer: function (seckillId) {
            return '/seckill/seckill/' + seckillId + '/exposer';
        },
        execution: function (seckillId, md5) {
            return '/seckill/seckill/' + seckillId + '/' + md5 + '/execution';

        }
    },
    handlerSeckillKill: function(seckillId, node) {
        //处理秒杀逻辑
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');//按钮
        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
            //在回调函数中执行交互流程
            if(result && result['success']) {
                var exposer = result['data'];
                if(exposer['exposed']) {
                    //开启秒杀
                    //获取秒杀地址
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    console.log('killUrl: ' + killUrl);
                    //绑定一次点击事件
                    $('#killBtn').one('click', function () {
                        //绑定执行秒杀请求的操作
                        //1。禁用按钮
                        $(this).addClass('disabled');
                        //2。发送秒杀请求，执行秒杀
                        $.post(killUrl, {}, function (result) {
                            if(result && result['success']) {
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                console.log('state: ' + state)
                                console.log('stateInfo: ' + stateInfo)
                                //显示秒杀结果
                                node.html('<span class="label label-success">'+ stateInfo +'</span>')
                            }
                        })
                    })
                    node.show();
                } else {
                    //未开启秒杀
                    /**
                     * 这里为什么还会出现未开启秒杀呢，不是在前端已经判断过了吗？
                     * 因为每个人的机器计时不同，jQuery的countdown插件计时的快慢其实有差异，所以这里会有些许差异，会
                     * 出现一些人计时结束，时间比服务器时间快的情况
                     */
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    //重新计算计时逻辑
                    seckill.countDown(seckillId, now, start, end);
                }
            } else {
                console.log('result: ' + result);
            }
        });
    },
    //验证手机号
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },
    countDown: function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $('#seckill-box');
        //时间判断
        if(nowTime > endTime) {
            //秒杀结束
            seckillBox.html('秒杀结束！')
        } else if(nowTime < startTime) {
            //秒杀未开始,事件绑定
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime, function (event) {
                //控制时间的格式
                var format = event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒');
                seckillBox.html(format);
                /*时间完成后回调事件*/
            }).on('finish.countdown', function () {
                //获取秒杀地址，控制显示秒杀逻辑，执行秒杀
                seckill.handlerSeckillKill(seckillId, seckillBox);
            });
        } else {
            //秒杀已开始
            seckill.handlerSeckillKill(seckillId, seckillBox);
        }
    },
    //详情页秒杀逻辑
    detail: {
        // 详情页初始化
        init: function (params) {
            // 用户手机验证和登陆， 计时交互
            // 规划交互流程
            // 在Cookie中查找手机号
            var killPhone = $.cookie('killPhone');
            //验证手机号
            if (!seckill.validatePhone(killPhone)) {
                //如果没有登陆为false，绑定phone
                //控制输出
                var killPhoneModal = $('#killPhoneModal');
                // 显示弹出层
                killPhoneModal.modal({
                    show: true, // 显示弹出层
                    backdrop: 'static', //禁止位置关闭
                    keyboard: false, //关闭键盘事件
                });
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    console.log('inputPhone=' + inputPhone);
                    if (seckill.validatePhone(inputPhone)) {
                        //电话写入cookie
                        $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'});
                        //刷新页面
                        window.location.reload();
                    } else {
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误</label>').show(300);
                    }
                });
            }
            //到这里已经登陆了
            //计时交互
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            $.get(seckill.URL.now(), {}, function (result) {
                if (result && result['success']) {
                    var nowTime = result['data'];
                    //时间判断
                    seckill.countDown(seckillId, nowTime, startTime, endTime)
                    console.log('result: ' + result);
                } else {
                    console.log('result: ' + result);
                }
            });
        }
    }
}