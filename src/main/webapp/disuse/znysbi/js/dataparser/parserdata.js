function shitu1(new_data) {
    // 小程序、app、微信号 新增
    let myChart = echarts.init(document.getElementById('shitu1'));
    let sum_new = parseInt(new_data[0]) + parseInt(new_data[1]) + parseInt(new_data[2]) + parseInt(new_data[3]) + parseInt(new_data[4]) + parseInt(new_data[5]) + parseInt(new_data[6]) + parseInt(new_data[7]);
    myChart.setOption({
        title: {
            text: '新增-' + sum_new,
            left: 'left',
            textStyle: {
                color: "#ccc"
            }
        },
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        series: [
            {
                name: '占比',
                type: 'pie',    // 设置图表类型为饼图
                radius: '55%',  // 饼图的半径，外半径为可视区尺寸（容器高宽中较小一项）的 55% 长度。
                data: [          // 数据数组，name 为数据项名称，value 为数据项值
                    {value: new_data[0], name: '快速问医生服务号'},
                    {value: new_data[1], name: '问医生服务号'},
                    {value: new_data[2], name: '快应用'},
                    {value: new_data[3], name: '支付宝小程序'},
                    {value: new_data[4], name: 'IOS'},
                    {value: new_data[5], name: '安卓'},
                    {value: new_data[6], name: '快速问医小程序'},
                    {value: new_data[7], name: '快速问医生小程序'},

                ]
            }
        ]
    })
}

function shitu2(money_data,total) {
    // 小程序、app、微信号 收入
    let dom = document.getElementById("shitu2");
    let myChart = echarts.init(dom);
    let app = {};
    let option = null;
    let a =
        option = {
            title: {
                text: '总收入-' + total[0],
                left: 'center',
                textStyle: {
                    color: "#ccc"
                }
            },
            tooltip: {
                trigger: 'item',
                formatter: '{a} <br/>{b}:({d}%)'
            },
            legend: {
                orient: 'vertical',
                left: 10,
                data: ['免费咨询:' + money_data[0], '指定咨询:' + money_data[1], '私人医生:' + money_data[2], '极速咨询:' + money_data[3], '视频门诊:' + money_data[4], '百度咨询:' + money_data[5], '医生推荐:' + money_data[6]]
            },
            series: [
                {
                    name: '占比',
                    type: 'pie',
                    radius: ['50%', '70%'],
                    avoidLabelOverlap: false,
                    label: {
                        show: false,
                        position: 'center'
                    },
                    emphasis: {
                        label: {
                            show: true,
                            fontSize: '30',
                            fontWeight: 'bold'
                        }
                    },
                    labelLine: {
                        show: false
                    },
                    data: [
                        {value: money_data[0], name: '免费咨询:' + money_data[0]},
                        {value: money_data[1], name: '指定咨询:' + money_data[1]},
                        {value: money_data[2], name: '私人医生:' + money_data[2]},
                        {value: money_data[3], name: '极速咨询:' + money_data[3]},
                        {value: money_data[4], name: '视频门诊:' + money_data[4]},
                        {value: money_data[5], name: '百度咨询:' + money_data[5]},
                        {value: money_data[6], name: '医生推荐:' + money_data[6]}
                    ]
                }
            ]
        };
    ;
    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }
}

function shitu3(live_data) {
    // 小程序、app、微信号 活跃
    let dom = document.getElementById("shitu3");
    let sum_live = parseInt(live_data[0]) + parseInt(live_data[1]) + parseInt(live_data[2]) + parseInt(live_data[3]) + parseInt(live_data[4]) + parseInt(live_data[5]) + parseInt(live_data[6]) + parseInt(live_data[7]);
    let myChart = echarts.init(dom);
    let app = {};
    let option = null;
    option = {
        title: {
            text: '活跃-' + sum_live,
            left: 'center',
            textStyle: {
                color: "#ccc"
            }
        },
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
            orient: 'vertical',
            left: 10,
            data: ['快速问医生服务号', '问医生服务号', '快应用', '支付宝小程序', 'IOS', '安卓', '快速问医生小程序', '快速问医小程序']
        },
        series: [
            {
                name: '占比',
                type: 'pie',
                radius: ['50%', '70%'],
                avoidLabelOverlap: false,
                label: {
                    show: false,
                    position: 'center'
                },
                emphasis: {
                    label: {
                        show: true,
                        fontSize: '30',
                        fontWeight: 'bold'
                    }
                },
                labelLine: {
                    show: false
                },
                data: [
                    {value: live_data[0], name: '快速问医生服务号'},
                    {value: live_data[1], name: '问医生服务号'},
                    {value: live_data[2], name: '快应用'},
                    {value: live_data[3], name: '支付宝小程序'},
                    {value: live_data[4], name: 'IOS'},
                    {value: live_data[5], name: '安卓'},
                    {value: live_data[6], name: '快速问医生小程序'},
                    {value: live_data[7], name: '快速问医小程序'},
                ]
            }
        ]
    };

    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }
}

function shitu4(re_live_data) {
    // 小程序、app、微信号 留存（新增、活跃）
    // 柱状图 咨询量 倒数第二个
    let myChart = echarts.init(document.getElementById('shitu4'));
    let sum_new = (parseFloat(re_live_data[0]) * 100 + parseFloat(re_live_data[2]) * 100 + parseFloat(re_live_data[4]) * 100 + parseFloat(re_live_data[6]) * 100 + parseFloat(re_live_data[8]) * 100 + parseFloat(re_live_data[10]) * 100 ) / 100;
    let sum_live = (parseFloat(re_live_data[1]) * 100 + parseFloat(re_live_data[3]) * 100 + parseFloat(re_live_data[5]) * 100 + parseFloat(re_live_data[7]) * 100 + parseFloat(re_live_data[9]) * 100 + parseFloat(re_live_data[11]) * 100 ) / 100;
    // 指定图表的配置项和数据
    let option = {
        title: {
            text: '一日后留存('+sum_new + '%,' + sum_live + '%)',
            left: 'left',
            textStyle: {
                color: "#ccc"
            }
        },
        legend: {},
        tooltip: {},
        dataset: {
            // 提供一份数据。
            source: [
                ['分类', '新增', '活跃'],
                ['快应用', re_live_data[0], re_live_data[1]],
                ['支付宝小程序', re_live_data[2], re_live_data[3]],
                ['IOS', re_live_data[4], re_live_data[5]],
                ['安卓', re_live_data[6], re_live_data[7]],
                ['快速问医生小程序', re_live_data[8], re_live_data[9]],
                ['快速问医小程序', re_live_data[10], re_live_data[11]]
            ]
        },
        // 声明一个 X 轴，类目轴（category）。默认情况下，类目轴对应到 dataset 第一列。
        xAxis: {
            type: 'category',
            axisTick: {
                alignWithLabel: true
            },
            axisLabel: {
                interval: 0,
                rotate:30
            }
        },
        // 声明一个 Y 轴，数值轴。
        yAxis: {},
        // 声明多个 bar 系列，默认情况下，每个系列会自动对应到 dataset 的每一列。
        series: [
            {type: 'bar'},
            {type: 'bar'}
        ]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
}

function shitu5(re_buy_data) {
    // 极速咨询、视频、药品 复购率
    let dom = document.getElementById("shitu5");
    let myChart = echarts.init(dom);
    let app = {};
    let option = null;
    let weatherIcons = {};
    option = {
        title: {
            text: '昨日复购-' + re_buy_data[3] + '%',
            left: 'left',
            textStyle: {
                color: "#ccc"
            }
        },
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : ({c}%)'
        },
        legend: {
            bottom: 0,
            left: 'center',
            // data: ['指定咨询', '私人医生', '极速咨询', '视频门诊', '药品', '打赏']
            data: ['指定咨询', '私人医生', '极速咨询']
        },
        series: [
            {
                name: '占比',
                type: 'pie',
                radius: '65%',
                center: ['50%', '50%'],
                selectedMode: 'single',
                data: [
                    {
                        value: parseFloat(re_buy_data[0]),
                        name: '指定咨询'
                    },
                    {value: parseFloat(re_buy_data[1]), name: '私人医生'},
                    {value: parseFloat(re_buy_data[2]), name: '极速咨询'}
                ],
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };


    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }
}

function shitu6(ask_data) {
    // 免费咨询、指定咨询、极速咨询 咨询量
    // 柱状图 咨询量 倒数第二个
    let myChart = echarts.init(document.getElementById('shitu6'));
    let sum_ask = parseInt(ask_data[0]) + parseInt(ask_data[1]) + parseInt(ask_data[2]) + parseInt(ask_data[3]) + parseInt(ask_data[4]) + parseInt(ask_data[5]);
    let option = {
        title: {
            text: '咨询量-' + sum_ask,
            left: 'left',
            textStyle: {
                color: "#ccc"
            }
        },
        tooltip: {},
        xAxis: {
            type: 'category',
            // data: ['免费咨询', '指定咨询', '私人医生', '极速咨询', '视频门诊', '百度咨询']
            data: ['免费咨询', '指定咨询', '极速咨询', '视频门诊', '百度咨询']
        },
        yAxis: {
            type: 'value'
        },
        series: [{
            data: [ask_data[0], ask_data[1], ask_data[3], ask_data[4], ask_data[5]],
            type: 'bar'
        }]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
}

function shitu7(money_back_data) {
    // 退款明细
    // 基于准备好的dom，初始化echarts实例
    let myChart = echarts.init(document.getElementById('shitu7'));
    myChart.setOption({
        title: {
            text: '退款',
            left: 'left',
            textStyle: {
                color: "#ccc"
            }
        },
        series: [
            {
                name: '访问来源',
                type: 'pie',    // 设置图表类型为饼图
                radius: '55%',  // 饼图的半径，外半径为可视区尺寸（容器高宽中较小一项）的 55% 长度。
                data: [          // 数据数组，name 为数据项名称，value 为数据项值
                    {value: 25, name: '退款数>' + money_back_data[1]},
                    {value: 25, name: '退款率>' + money_back_data[0]},
                    {value: 25, name: '冻结率>' + money_back_data[2]},
                    {value: 25, name: '退款金额>' + money_back_data[3]},

                ]
            }
        ]
    })
}
