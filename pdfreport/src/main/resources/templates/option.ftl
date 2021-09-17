{
    title: {
        text: '${title}'
    },
    legend: {
        data: ['您的层级', '平均层级'],
        left:'right'
    },
    radar: {
        indicator: [
            { name: '销售（Sales）', max: 6500},
            { name: '管理（Administration）', max: 16000},
            { name: '信息技术（Information Technology）', max: 30000},
            { name: '客服（Customer Support）', max: 38000},
            { name: '研发（Development）', max: 52000},
            { name: '市场（Marketing）', max: 25000}
        ]
    },
    series: [{
        name: '您的层级 vs 平均层级',
        type: 'radar',
        data: [
            {
            value: ${yours},
            name: '您的层级'
            },
            {
            value: ${average},
            name: '平均层级'
            }
        ]
    }]
}