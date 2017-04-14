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
<!--   <link href="./bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen"> -->
<link href="./css/bootstrap-datetimepicker.min.css" rel="stylesheet"
	media="screen">
<style type="text/css">
body {
	background-color: rgba(204, 204, 204, 0.4);
}

.chart-border {
	width: 100%;
	height: 350px;
	border: 1px;
	border-style: dashed;
	border-color: #dcd0d0;
	padding: 5px;
}
.user-border {
	width: 100%;
	height: 450px;
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

.tip {
	background-color: rgba(150, 144, 144, 0.23);
	margin: 10px;
	color: #7f1d1d;
}
</style>
</head>

<body>
	<div class="container-fluid">
		<div class="row chart-row">
			<div class="col-md-4">
				<form class="form-inline" role="form">
					<div class="input-group">
						<label for="dtp_input1" class="control-label">开始时间</label>
						<div id="start" class="input-group date form_date" data-date=""
							data-date-format="MM-dd-yyyy" data-link-field="dtp_input1"
							data-link-format="yyyymmdd">
							<input class="form-control" size="16" type="text" value=""
								readonly> <span class="input-group-addon"><span
								class="glyphicon glyphicon-remove"></span></span> <span
								class="input-group-addon"><span
								class="glyphicon glyphicon-calendar"></span></span>
						</div>
						<input type="hidden" id="dtp_input1" value="" />
					</div>
				</form>
			</div>
			<div class="col-md-4">
				<form class="form-inline" role="form">
					<div class="input-group">
						<label for="dtp_input2" class="control-label">结束时间</label>
						<div id="end" class="input-group date form_date" data-date=""
							data-date-format="MM-dd-yyyy" data-link-field="dtp_input2"
							data-link-format="yyyymmdd">
							<input class="form-control" size="16" type="text" value=""
								readonly> <span class="input-group-addon"><span
								class="glyphicon glyphicon-remove"></span></span> <span
								class="input-group-addon"><span
								class="glyphicon glyphicon-calendar"></span></span>
						</div>
						<input type="hidden" id="dtp_input2" value="" /><br />
					</div>
				</form>
			</div>
			<div class="col-md-2">
				<div class="form-inline" role="">
					<button class="btn btn-primary" onClick="search()">查询</button>
				</div>
			</div>
		</div>
		<div class="row chart-row">
			<!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
			<div class="col-md-6">
				<div>
					<p class="tip">
						共计<span id="total"></span>条数据，命中规则<span id="hit"></span>条数据，命中率<span
							id="hitRate"></span>%；充值环节数据<span id="reCharge"></span>, 占比<span
							id="reChargeRate"></span>%；
					</p>
				</div>
				<div id="main1" class="chart-border"></div>
			</div>
			<div class="col-md-6">
				<div>
					<p class="tip">
						命中规则的用户<span id="userTotal"></span>条，其中风险等级在10+以上用户占比<span
							id="highRate"></span>%
					</p>
				</div>
				<div id="main2" class="chart-border"></div>
			</div>
		</div>
		<div class="row chart-row">
			<div class="col-md-6">
				<div>
					<p class="tip">
						累计分值在20分以上用户账号数量为<span id="accountTotal"></span>个
					</p>
				</div>
				<div id="main3" class="table-border">
					<table id="account" class="table table-bordered table-hover">
						<tr class="active">
							<th>排名</th>
							<th>帐号</th>
							<th>分值</th>
						</tr>
					</table>
				</div>
			</div>
			<div class="col-md-6">
				<div>
					<p class="tip">
						累计分值在20分以上用户IP数量为<span id="ipTotal"></span>个
					</p>
				</div>
				<div class="table-border">
					<table id="ip" class="table table-bordered table-hover">
						<tr class="active">
							<th>排名</th>
							<th>IP</th>
							<th>分值</th>
						</tr>
					</table>
				</div>
			</div>
		</div>
		<div class="row chart-row">
			<div class="col-md-6">
				<div>
					<p class="tip">
						累计最高风险充值时间段为<span id="timeFirst"></span>点
					</p>
				</div>
				<div class="table-border">
					<table id="time" class="table table-bordered table-hover">
						<tr class="active">
							<th>排名</th>
							<th>时</th>
							<th>分值</th>
						</tr>
					</table>
				</div>
			</div>
			<div class="col-md-6">
				<div>
					<p class="tip">
						累计分值在20分以上设备数量为<span id="devTotal"></span>个
					</p>
				</div>
				<div class="table-border">
					<table id="dev" class="table table-bordered table-hover">
						<tr class="active">
							<th>排名</th>
							<th>设备指纹</th>
							<th>分值</th>
						</tr>
					</table>
				</div>
			</div>
		</div>
		<div class="row chart-row">
			<div class="col-md-12">
				<div id="main5" class="user-border"></div>
			</div>
		</div>
	</div>
	<script type="text/javascript"
		src="./js/bootstrap-datetimepicker.min.js" charset="UTF-8"></script>
	<script type="text/javascript"
		src="./js/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
	<script type="text/javascript">
	    start = $("#dtp_input1").val();
		end = $("#dtp_input2").val();
		if (parseInt(start) >= parseInt(end)) {
			alert("开始时间不能大于结束时间！")
		} else {
			report(start, end);
		}

		$('#start').datetimepicker({
			language : 'fr',
			weekStart : 1,
			todayBtn : 1,
			autoclose : 1,
			todayHighlight : 1,
			startView : 2,
			minView : 2,
			forceParse : 0
		});
		$('#end').datetimepicker({
			language : 'fr',
			weekStart : 1,
			todayBtn : 1,
			autoclose : 1,
			todayHighlight : 1,
			startView : 2,
			minView : 2,
			forceParse : 0
		});
	function search() {
		start = $("#dtp_input1").val();
		end = $("#dtp_input2").val();
		if (parseInt(start) >= parseInt(end)) {
			alert("开始时间不能大于结束时间！")
		} else {
			report(start, end);
		}
	};
	function report(start, end) {
		var ipUrl = '/history/esRiskIp/get.ajax';
		var accountUrl = '/history/esRiskAccount/get.ajax';
		var timeUrl = '/history/esRiskTime/get.ajax';
		var devUrl = '/history/esRiskDev/get.ajax';
		$.ajax({
			url : ipUrl,
			data : {
				"start" : start,
				"end" : end
			},
			type : 'POST',
			dataType : 'json',
			cache : false,
			async : true,
			success : function(data) {
				if (data.status) {
					$("#ipTotal").empty();
					$("#ipTotal").append(data.data.ipTotal);
					ipScoreList = data.data.ipScore;
					var ipData = [];
					for ( var key in ipScoreList) {
						/* console.log("属性：" + key); // 输出如下图所示
						console.log("值："); // 输出如下图所示
						console.log(ipScoreList[key]); */
						ipData.push(ipScoreList[key].ipScore);
					}
					$("#ip tr:gt(0)").empty();
					for ( var key in ipData) {
						if (parseInt(key) >= 15) {
							break;
						}
						var $tr = $("#ip tr").eq(-1);
						var ipHtml;
						switch (parseInt(key)) {
						case 0:
						case 1:
						case 2:
							ipHtml = "<tr class='danger'><td>"
									+ (parseInt(key) + 1) + "</td><td>"
									+ ipData[key].ip + "</td><td>"
									+ ipData[key].score + "</td></tr>"
							break;
						case 3:
						case 4:
						case 5:
							ipHtml = "<tr class='warning'><td>"
									+ (parseInt(key) + 1) + "</td><td>"
									+ ipData[key].ip + "</td><td>"
									+ ipData[key].score + "</td></tr>"
							break;
						case 6:
						case 7:
						case 8:
							ipHtml = "<tr class='info'><td>" + (parseInt(key) + 1)
									+ "</td><td>" + ipData[key].ip + "</td><td>"
									+ ipData[key].score + "</td></tr>"
							break;
						case 9:
						case 10:
						case 11:
							ipHtml = "<tr class='active'><td>"
									+ (parseInt(key) + 1) + "</td><td>"
									+ ipData[key].ip + "</td><td>"
									+ ipData[key].score + "</td></tr>"
							break;
						case 12:
						case 13:
						case 14:
							ipHtml = "<tr class='success'><td>"
									+ (parseInt(key) + 1) + "</td><td>"
									+ ipData[key].ip + "</td><td>"
									+ ipData[key].score + "</td></tr>"
							break;
						}
						$tr.after(ipHtml);
					}
				} else {
					alert("获取ip信息失败！")
				}
			}
		});
		$.ajax({
			url : accountUrl,
			data : {
				"start" : start,
				"end" : end
			},
			type : 'POST',
			dataType : 'json',
			cache : false,
			async : true,
			success : function(data) {
				if (data.status) {
					$("#accountTotal").empty();
					$("#accountTotal").append(data.data.accountTotal);
					accountScoreList = data.data.accountScore;
					var accountData = [];
					for ( var key in accountScoreList) {
						/* console.log("属性：" + key); // 输出如下图所示
						console.log("值："); // 输出如下图所示
						console.log(accountScoreList[key]); */
						accountData.push(accountScoreList[key].accountScore);
					}
					$("#account tr:gt(0)").empty();
					for ( var key in accountData) {
						if (parseInt(key) >= 15) {
							break;
						}
						var $tr = $("#account tr").eq(-1);
						var accountHtml;
						switch (parseInt(key)) {
						case 0:
						case 1:
						case 2:
							accountHtml = "<tr class='danger'><td>"
									+ (parseInt(key) + 1) + "</td><td>"
									+ accountData[key].account + "</td><td>"
									+ accountData[key].score + "</td></tr>"
							break;
						case 3:
						case 4:
						case 5:
							accountHtml = "<tr class='warning'><td>"
									+ (parseInt(key) + 1) + "</td><td>"
									+ accountData[key].account + "</td><td>"
									+ accountData[key].score + "</td></tr>"
							break;
						case 6:
						case 7:
						case 8:
							accountHtml = "<tr class='info'><td>"
									+ (parseInt(key) + 1) + "</td><td>"
									+ accountData[key].account + "</td><td>"
									+ accountData[key].score + "</td></tr>"
							break;
						case 9:
						case 10:
						case 11:
							accountHtml = "<tr class='active'><td>"
									+ (parseInt(key) + 1) + "</td><td>"
									+ accountData[key].account + "</td><td>"
									+ accountData[key].score + "</td></tr>"
							break;
						case 12:
						case 13:
						case 14:
							accountHtml = "<tr class='success'><td>"
									+ (parseInt(key) + 1) + "</td><td>"
									+ accountData[key].account + "</td><td>"
									+ accountData[key].score + "</td></tr>"
							break;
						}
						$tr.after(accountHtml);
					}
				} else {
					alert("获取数据失败！")
				}
			}
		});
		$.ajax({
			url : timeUrl,
			data : {
				"start" : start,
				"end" : end
			},
			type : 'POST',
			dataType : 'json',
			cache : false,
			async : true,
			success : function(data) {
				if (data.status) {
					timeScoreList = data.data.timeScore;
					var timeData = [];
					for ( var key in timeScoreList) {
						console.log("属性：" + key); // 输出如下图所示
						console.log("值："); // 输出如下图所示
						console.log(timeScoreList[key]);
						timeData.push(timeScoreList[key].timeScore);
					}
					$("#time tr:gt(0)").empty();
					for ( var key in timeData) {
						if (parseInt(key) >= 15) {
							break;
						}
						var $tr = $("#time tr").eq(-1);
						var timeHtml;
						switch (parseInt(key)) {
						case 0:
							$("#timeFirst").empty();
							$("#timeFirst").append(timeData[key].time);
						case 1:
						case 2:
							timeHtml = "<tr class='danger'><td>"
									+ (parseInt(key) + 1) + "</td><td>"
									+ timeData[key].time + "</td><td>"
									+ timeData[key].score + "</td></tr>"
							break;
						case 3:
						case 4:
						case 5:
							timeHtml = "<tr class='warning'><td>"
									+ (parseInt(key) + 1) + "</td><td>"
									+ timeData[key].time + "</td><td>"
									+ timeData[key].score + "</td></tr>"
							break;
						case 6:
						case 7:
						case 8:
							timeHtml = "<tr class='info'><td>"
									+ (parseInt(key) + 1) + "</td><td>"
									+ timeData[key].time + "</td><td>"
									+ timeData[key].score + "</td></tr>"
							break;
						case 9:
						case 10:
						case 11:
							timeHtml = "<tr class='active'><td>"
									+ (parseInt(key) + 1) + "</td><td>"
									+ timeData[key].time + "</td><td>"
									+ timeData[key].score + "</td></tr>"
							break;
						case 12:
						case 13:
						case 14:
							timeHtml = "<tr class='success'><td>"
									+ (parseInt(key) + 1) + "</td><td>"
									+ timeData[key].time + "</td><td>"
									+ timeData[key].score + "</td></tr>"
							break;
						}
						$tr.after(timeHtml);
					}
				} else {
					alert("获取time数据失败！")
				}
			}
		});
		$.ajax({
			url : devUrl,
			data : {
				"start" : start,
				"end" : end
			},
			type : 'POST',
			dataType : 'json',
			cache : false,
			async : true,
			success : function(data) {
				if (data.status) {
					$("#devTotal").empty();
					$("#devTotal").append(data.data.devTotal);
					devScoreList = data.data.devScore;
					var devData = [];
					for ( var key in devScoreList) {
						console.log("属性：" + key); // 输出如下图所示
						console.log("值："); // 输出如下图所示
						console.log(devScoreList[key]);
						devData.push(devScoreList[key].devScore);
					}
					$("#dev tr:gt(0)").empty();
					for ( var key in devData) {
						if (parseInt(key) >= 15) {
							break;
						}
						var $tr = $("#dev tr").eq(-1);
						var devHtml;
						switch (parseInt(key)) {
						case 0:
						case 1:
						case 2:
							devHtml = "<tr class='danger'><td>"
									+ (parseInt(key) + 1) + "</td><td>"
									+ devData[key].dev + "</td><td>"
									+ devData[key].score + "</td></tr>"
							break;
						case 3:
						case 4:
						case 5:
							devHtml = "<tr class='warning'><td>"
									+ (parseInt(key) + 1) + "</td><td>"
									+ devData[key].dev + "</td><td>"
									+ devData[key].score + "</td></tr>"
							break;
						case 6:
						case 7:
						case 8:
							devHtml = "<tr class='info'><td>" + (parseInt(key) + 1)
									+ "</td><td>" + devData[key].dev + "</td><td>"
									+ devData[key].score + "</td></tr>"
							break;
						case 9:
						case 10:
						case 11:
							devHtml = "<tr class='active'><td>"
									+ (parseInt(key) + 1) + "</td><td>"
									+ devData[key].dev + "</td><td>"
									+ devData[key].score + "</td></tr>"
							break;
						case 12:
						case 13:
						case 14:
							devHtml = "<tr class='success'><td>"
									+ (parseInt(key) + 1) + "</td><td>"
									+ devData[key].dev + "</td><td>"
									+ devData[key].score + "</td></tr>"
							break;
						}
						$tr.after(devHtml);
					}
				} else {
					alert("获取设备信息失败！")
				}
			}
		});
		// 基于准备好的dom，初始化echarts实例
		var myChart1 = echarts
				.init(document.getElementById('main1'), 'macaron');
		var ensembleUrl = '/history/esEnsemble/get.ajax';

		$.ajax({
			url : ensembleUrl,
			data : {
				"start" : start,
				"end" : end
			},
			type : 'POST',
			dataType : 'json',
			cache : false,
			async : true,
			success : function(data) {
				if (data.status) {
					$("#total").empty();
					$("#total").append(data.data.total);
					$("#hit").empty();
					$("#hit").append(data.data.hit);
					$("#reCharge").empty();
					$("#reCharge").append(data.data.reCharge);
					var hitRate = data.data.hitRate;
					hitRate = (hitRate * 100).toFixed(2);
					$("#hitRate").empty();
					$("#hitRate").append(hitRate);
					$("#reChargeRate").empty();
					$("#reChargeRate").append(
							(data.data.reChargeRate * 100).toFixed(2));
					var colors = [ 'orange', '#d14a61', '#1E90FF' ];
					var option1 = {
						color : colors,
						tooltip : {
							trigger : 'axis',
							axisPointer : { // 坐标轴指示器，坐标轴触发有效
								type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
							}
						},
						grid : {
							right : '20%'
						},
						title : {
							text : '运营数据规模',
							x : 'center'
						},
						toolbox : {
							feature : {
								dataView : {
									show : true,
									readOnly : false
								},
								restore : {
									show : true
								},
								saveAsImage : {
									show : true
								}
							}
						},
						legend : {
							data : [ '总量', '充值量', '命中率' ],
							left : 'left'
						},
						xAxis : [ {
							type : 'category',
							axisTick : {
								alignWithLabel : true
							},
							// data: ['20170322', '20170323', '20170324', '20170325', '20170327', '20170328', '20170329', '20170329', '20170401', '20170402', '20170403', '20170404']
							data : data.data.xAxis
						} ],
						yAxis : [ {
							type : 'value',
							name : '命中率',
							/*  min: 0,
							 max: 250, */
							position : 'right',
							axisLine : {
								lineStyle : {
									color : colors[0]
								}
							},
							axisLabel : {
								formatter : '{value} ％'
							}
						}, {
							type : 'value',
							name : '总量',
							// min: 0,
							// max: 250,
							position : 'left',
							axisLine : {
								lineStyle : {
									color : colors[2]
								}
							},
							axisLabel : {
							// formatter: '{value} °C'
							}
						} ],
						dataZoom : [ {
						// startValue: '20170325'
						}, {
							type : 'inside'
						} ],
						series : [ {
							name : '总量',
							type : 'bar',
							yAxisIndex : 1,
							// data: [2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2]
							data : data.data.seriesTotalAccount
						}, {
							name : '充值量',
							type : 'bar',
							yAxisIndex : 1,
							data : data.data.seriesRechargeAccount
						}, {
							name : '命中率',
							type : 'line',
							data : data.data.seriesHitRate
						} ]
					};
					myChart1.setOption(option1);
					// barChart.setOption(option, true)

				} else {
					alert("获取失败！")
				}
			}
		});

		var myChart2 = echarts
				.init(document.getElementById('main2'), 'macaron');
		var userDistributionUrl = '/history/esUserDistribution/get.ajax';

		$.ajax({
			url : userDistributionUrl,
			data : {
				"start" : start,
				"end" : end
			},
			type : 'POST',
			dataType : 'json',
			cache : false,
			async : true,
			success : function(data) {
				$("#userTotal").empty();
				$("#userTotal").append(data.data.total);
				$("#highRate").empty();
				$("#highRate").append((data.data.highRate * 100).toFixed(2));
				var obj = data.data.seriesList;
				var seriesData = [];
				for ( var key in obj) {
					/* console.log("属性：" + key);
					console.log("值：")
					console.log(obj[key]); */
					seriesData.push(obj[key].series);
				}
				// console.log(seriesData);
				if (data.status) {
					var option2 = {
						title : {
							text : '风险等级用户比例分布',
							x : 'center'
						},
						toolbox : {
							feature : {
								dataView : {
									show : true,
									readOnly : false
								},
								restore : {
									show : true
								},
								saveAsImage : {
									show : true
								}
							},
							left: 'left'
						},
						tooltip : {
							trigger : 'item',
							formatter : "{a} <br/>{b} : {c} ({d}%)"
						},
						legend : {
							orient : 'vertical',
							left : 'right',
							// data: ['0~5','6~10','11~20','20+']
							data : data.data.legend
						},
						series : [ {
							name : '风险等级',
							type : 'pie',
							radius : '55%',
							center : [ '50%', '60%' ],
							/*  data:[
							     {value:300, name:'0~5'},
							     {value:310, name:'6~10'},
							     {value:2342, name:'11~20'},
							     {value:1357, name:'20+'},
							 ], */
							data : seriesData,
							itemStyle : {
								emphasis : {
									shadowBlur : 10,
									shadowOffsetX : 0,
									shadowColor : 'rgba(0, 0, 0, 0.5)'
								},
								normal : {
									label : {
										show : true,
										formatter : '{b} : {c} ({d}%)'
									},
									labelLine : {
										show : true
									}
								}
							}
						} ]
					};
					myChart2.setOption(option2);
				}
			}
		});

		var myChart3 = echarts
				.init(document.getElementById('main5'), 'macaron');
		var colors = [ 'orange', '#d14a61', '#1E90FF' ];
		var ruleUrl = '/history/esRiskRule/get.ajax';
		$.ajax({
			url : ruleUrl,
			data : {
				"start" : start,
				"end" : end
			},
			type : 'POST',
			dataType : 'json',
			cache : false,
			async : true,
			success : function(data) {
				var option3 = {
					color : colors,
					tooltip : {
						trigger : 'axis'
					},
					grid : {
						left : '20%',
						bottom : '35%'
					},
					title : {
						text : '风险规则命中情况',
						x : 'center'
					},
					toolbox : {
						feature : {
							dataView : {
								show : true,
								readOnly : false
							},
							restore : {
								show : true
							},
							saveAsImage : {
								show : true
							}
						}
					},
					/*  legend: {
					     data: ['命中数'],
					     left: 'left'
					 }, */
					xAxis : [ {
						type : 'category',
						axisTick : {
							alignWithLabel : true
						},
						axisLabel : {
							show : true,
							interval : 0, // {number}
							rotate : 35,
						// textStyle: {
						//     color: 'blue',
						//     fontFamily: 'sans-serif',
						//     fontSize: 15,
						//     fontStyle: 'italic',
						//     fontWeight: 'bold'
						// }
						},
						// nameRotate: 120,
						// data: ['设备已Root', '1天内，同一账号充值使用设备', '1小时内，同一账号充值使用设备', '30分钟内，账号跨城市移动', '1天内，同一账号充值使用设备', '1天内，同一账号充值使用设备', '1天内，同一账号充值使用设备', '1天内，同一账号充值使用设备', '1天内，同一账号充值使用设备', '1天内，同一账号充值使用设备', '1天内，同一账号充值使用设备', '1天内，同一账号充值使用设备']
						data : data.data.xAxis
					} ],
					yAxis : [ {
						type : 'value',
						name : '命中数',
						position : 'left',
						axisLine : {
							lineStyle : {
								color : colors[0]
							}
						},
						axisLabel : {
						// formatter: '{value} ％'
						}
					} ],
					series : [ {
						name : '命中数',
						type : 'bar',
						// yAxisIndex: ,
						// data: [2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2]
						data : data.data.series
					} ]
				};
				myChart3.setOption(option3);
				// barChart.setOption(option, true)
			}
		});		
	}
	</script>
</body>
</html>