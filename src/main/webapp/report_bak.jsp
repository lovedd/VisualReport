<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <!-- 引入 ECharts 文件 -->
    <script src="./js/echarts.js"></script>
    <script type="text/javascript" src="./js/macarons.js"></script>
    <script type="text/javascript" src="./js/dark.js"></script>
<!--     <script type="text/javascript" src="./js/shine.js"></script>
 -->
    <script type="text/javascript" src="./js/jquery-3.2.0.min.js"></script>
    <script type="text/javascript" src="./js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="./css/bootstrap.min.css">
    <style type="text/css">
        body {
            background-color: rgba(204, 204, 204, 0.4);
        }
        .chart-border {
            width: 100%;
            height:450px; 
            border: 1px;
            border-style: dashed;
            border-color: #dcd0d0;
            padding: 5px;
        }
        .table-border {
            width: 100%;
            height: 565px; 
            border: 1px;
            border-style: dashed;
            border-color: #dcd0d0;
            padding: 5px;
        }
        .chart-row {
            padding-top: 5px;
        }
    </style>
</head>

<body>
    <div class="container-fluid">
        <div class="row chart-row">
            <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
            <div class="col-md-6">
                <div id="main1" class="chart-border"></div>
            </div>
            <div class="col-md-6">
                <div id="main2" class="chart-border"></div>
            </div>
        </div>
        <div class="row chart-row">
            <div class="col-md-6">
                <div id="main3" class="table-border">
                    <table class="table table-bordered table-hover">
                        <tr class="active">
                            <th>排名</th>
                            <th>IP</th>
                            <th>分值</th>
                        </tr>
                        <tr class="danger">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="danger">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                         <tr class="danger">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="warning">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="warning">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="warning">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="info">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="info">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="info">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="active">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="active">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="active">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="success">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="success">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="success">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                    </table>
                </div>
            </div>


            <div class="row chart-row">
                <div class="col-md-6">
                    <div id="main3" class="table-border">
                        <table id="account" class="table table-bordered table-hover">
                            <tr class="active">
                            <th>排名</th>
                            <th>帐号</th>
                            <th>分值</th>
                            </tr>
                            <tr class="success">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>









            <div class="col-md-6">
                <div id="main4" class="table-border">
                    <table class="table table-bordered table-hover">
                        <tr class="active">
                            <th>排名</th>
                            <th>时</th>
                            <th>分值</th>
                            <th>排名</th>
                            <th>设备指纹</th>
                            <th>分值</th>
                        </tr>
                        <tr class="danger">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="danger">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                         <tr class="danger">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="warning">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="warning">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="warning">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="info">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="info">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="info">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="active">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="active">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="active">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="success">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="success">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                        <tr class="success">
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
        <div class="row chart-row">
            <div class="col-md-6">
                <div id="main5" class="chart-border"></div>
            </div>
        </div>
    </div>
    <script type="text/javascript">
        console.log("oklala")
		var url = "/history/hello/login.ajax"
        
        $.get(url).done(function (data) {
        	console.log(data);
        })

        /* $.get({
            url: url,
            function(data) {
                console.log(data);
            }
        }).always(function(data) {
            console.log(data);
          }); */
         for(var i = 0; i < 10; i++) {
            var $tr=$("#account tr").eq(-1);
            var trHtml="<tr class='success'><td>"+ i +"</td><td>guohaifeng</td><td>guohaifeng</td></tr>";
            $tr.after(trHtml);
         }
        
         console.log("oklala=====")
        // app.title = '多 Y 轴示例';
        // 基于准备好的dom，初始化echarts实例

        var myChart1 = echarts.init(document.getElementById('main1'), 'macaron');
		var url1 = '';
		
		$.ajax({
			url : '/history/esEnsemble/get.ajax',	
			data : {
				"start" : "",
				"end" : ""
			},
			type : 'POST',
			dataType : 'json',
			cache : false,
			async : true,
			success : function(data) {
				if (data.status) {
					var colors = ['orange', '#d14a61', '#1E90FF'];
		        	console.log(data);
			        var option1 = {
			            color: colors,
			
			            tooltip: {
			                trigger: 'axis',
			                axisPointer: { // 坐标轴指示器，坐标轴触发有效
			                    type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
			                }
			            },
			            grid: {
			                right: '20%'
			            },
			            title: {
			                text: '运营数据规模',
			                x: 'center'
			            },
			            toolbox: {
			                feature: {
			                    dataView: {
			                        show: true,
			                        readOnly: false
			                    },
			                    restore: {
			                        show: true
			                    },
			                    saveAsImage: {
			                        show: true
			                    }
			                }
			            },
			            legend: {
			                data: [ '总量', '充值量', '命中率'],
			                left: 'left'
			            },
			            xAxis: [{
			                type: 'category',
			                axisTick: {
			                    alignWithLabel: true
			                },
			                // data: ['20170322', '20170323', '20170324', '20170325', '20170327', '20170328', '20170329', '20170329', '20170401', '20170402', '20170403', '20170404']
			            	data: data.data.xAxis
			            }],
			            yAxis: [{
			                type: 'value',
			                name: '命中率',
			               /*  min: 0,
			                max: 250, */
			                position: 'right',
			                axisLine: {
			                    lineStyle: {
			                        color: colors[0]
			                    }
			                },
			                axisLabel: {
			                    formatter: '{value} ％'
			                }
			            },  {
			                type: 'value',
			                name: '总量',
			                // min: 0,
			                // max: 250,
			                position: 'left',
			                axisLine: {
			                    lineStyle: {
			                        color: colors[2]
			                    }
			                },
			                axisLabel: {
			                    // formatter: '{value} °C'
			                }
			            }],
			            dataZoom: [{
			               // startValue: '20170325'
			            }, {
			               type: 'inside'
			            }],
			            series: [{
			                name: '总量',
			                type: 'bar',
			                yAxisIndex: 1,
			                // data: [2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2]
			            	data: data.data.seriesTotalAccount
			            }, {
			                name: '充值量',
			                type: 'bar',
			                yAxisIndex: 1,
			                data: data.data.seriesRechargeAccount
			            }, {
			                name: '命中率',
			                type: 'line',
			                data:data.data.seriesHitRate
			            }]
			        };
			        myChart1.setOption(option1);
				} else {
					alert("删除失败！")
				}
			}
		});
		
        $.get(url).done(function (data) {
        	
        })

        var myChart2 = echarts.init(document.getElementById('main2'), 'macaron');
        var option2 = {
            title : {
                text: '风险等级用户比例分布',
                // subtext: '纯属虚构',
                x:'center'
            },
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                left: 'right',
                data: ['0~5','6~10','11~20','20+']
            },
            series : [
                {
                    name: '风险等级',
                    type: 'pie',
                    radius : '55%',
                    center: ['50%', '60%'],
                    data:[
                        {value:300, name:'0~5'},
                        {value:310, name:'6~10'},
                        {value:2342, name:'11~20'},
                        {value:1357, name:'20+'},
                    ],
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        },
                        normal:{ 
                        label:{ 
                            show: true, 
                            formatter: '{b} : {c} ({d}%)' 
                          }, 
                          labelLine :{show:true} 
                        } 
                    }
                }
            ]
        };
        myChart2.setOption(option2);

        // app.title = '多 Y 轴示例';
        // 基于准备好的dom，初始化echarts实例
        var myChart3 = echarts.init(document.getElementById('main5'), 
            'macaron');

        var colors = ['orange', '#d14a61', '#1E90FF'];

        var option3 = {
            color: colors,

            tooltip: {
                trigger: 'axis'
            },
            grid: {
                left: '20%',
                bottom: '25%'
            },
            title: {
                text: '运营数据规模',
                x: 'center'
            },
            toolbox: {
                feature: {
                    dataView: {
                        show: true,
                        readOnly: false
                    },
                    restore: {
                        show: true
                    },
                    saveAsImage: {
                        show: true
                    }
                }
            },
            legend: {
                data: [ '总量', '充值量', '命中率'],
                left: 'left'
            },
            xAxis: [{
                type: 'category',
                axisTick: {
                    alignWithLabel: true
                },
                axisLabel:{
                    show:true,
                    interval: 0,    // {number}
                    rotate: 30,
                    // textStyle: {
                    //     color: 'blue',
                    //     fontFamily: 'sans-serif',
                    //     fontSize: 15,
                    //     fontStyle: 'italic',
                    //     fontWeight: 'bold'
                    // }
                },
                // nameRotate: 120,
                data: ['设备已Root', '1天内，同一账号充值使用设备', '1小时内，同一账号充值使用设备', '30分钟内，账号跨城市移动', '1天内，同一账号充值使用设备', '1天内，同一账号充值使用设备', '1天内，同一账号充值使用设备', '1天内，同一账号充值使用设备', '1天内，同一账号充值使用设备', '1天内，同一账号充值使用设备', '1天内，同一账号充值使用设备', '1天内，同一账号充值使用设备']
            }],
            yAxis: [{
                type: 'value',
                name: '命中数',
                min: 0,
                max: 250,
                position: 'left',
                axisLine: {
                    lineStyle: {
                        color: colors[0]
                    }
                },
                axisLabel: {
                    formatter: '{value} ％'
                }
            }],
            series: [{
                name: '总量',
                type: 'bar',
                // yAxisIndex: ,
                data: [2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2]
            }]
        };
        myChart3.setOption(option3);

    </script>
</body>
</html>