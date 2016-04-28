var globalstock,globaldata;
$(function(){

   console.log("In here");

});


$(document).ready(function() {

    var stockData=Android.getStockData();
    var symbol=Android.getSymbol();
    console.log("er1"+symbol);
    $.fn.highstocktab(JSON.parse(stockData),symbol);


});






$.fn.highstocktab = function(obj,symbol) {


       if (obj.Elements[0])
            Y_axis = obj.Elements[0].DataSeries.close.values;
        hist = [];
        for (i = 0; obj.Dates && i < obj.Dates.length; i++) {
            //console.log("in forloop");
            var date = new Date(obj.Dates[i]);
            hist.push([Date.UTC(date.getFullYear(), date.getMonth(), date.getDate()), Y_axis[i]]);
        }
        console.log("hisstory length"+hist.length);
        if (hist.length > 0) {
            $('#history').highcharts('StockChart', {
                rangeSelector: {
                    selected: 0
                    , inputEnabled: false
                    , buttons: [{
                        type: 'week'
                        , count: 1
                        , text: '1w'
                    }, {
                        type: 'month'
                        , count: 1
                        , text: '1m'
                    }, {
                        type: 'month'
                        , count: 3
                        , text: '3m'
                    }, {
                        type: 'month'
                        , count: 6
                        , text: '6m'
                    }, {
                        type: 'ytd'
                        , text: 'YTD'
                    }, {
                        type: 'year'
                        , count: 1
                        , text: '1y'
                    }, {
                        type: 'all'
                        , text: 'All'
                    }]
                },
                charts:{
                    width:undefined,
                    reflow: true
                }
                , yAxis: [{
                    title: {
                        text: 'Stock Value'
                    },min:0}]

                , title: {
                    text: symbol + ' Stock Value'
                }
                , series: [{
                    name: symbol.toUpperCase()
                    , data: hist
                    , type: 'area'
                    , threshold: null
                    , tooltip: {
                        valueDecimals: 2
                        , valuePrefix: "$"
                    }
                    , fillColor: {
                        linearGradient: {
                              x1: 0
                            , y1: 0
                            , x2: 0
                            , y2: 1
                        }
                        , stops: [
                        [0, Highcharts.getOptions().colors[0]]

                            , [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                        ]
                    }
                }]
            });
        }
/*Highcharts.setOptions({
        chart: {
                style: {
                    width: '100px',
                    height:'100px'
        }
    }
});*/

};
