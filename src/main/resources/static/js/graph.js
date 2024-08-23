document.addEventListener('DOMContentLoaded', function () {
function fetchRatioData() {
    $.get('/api/ratio', function(data) {
        console.log('Ratio Data:', data);

        // 데이터 형식 확인 및 숫자로 변환
        if (data && !isNaN(parseFloat(data.jobPostPercentage)) && !isNaN(parseFloat(data.applicationPercentage))) {
            const jobPostPercentage = parseFloat(data.jobPostPercentage);
            const applicationPercentage = parseFloat(data.applicationPercentage);

            const ctx = document.getElementById('ratio-chart');
            if (!ctx) {
                console.error('Canvas element "ratio-chart" not found');
                return;
            }

            new Chart(ctx.getContext('2d'), {
                type: 'pie',
                data: {
                    labels: ['채용 공고', '지원서'],
                    datasets: [{
                        data: [jobPostPercentage, applicationPercentage],
                        backgroundColor: ['#4e73df', '#1cc88a'],
                        hoverBackgroundColor: ['#2e59d9', '#17a673'],
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            position: 'top',
                        },
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    let label = context.label || '';
                                    if (label) {
                                        label += ': ';
                                    }
                                    if (context.parsed !== null) {
                                        label += context.parsed.toFixed(1) + '%';
                                    }
                                    return label;
                                }
                            }
                        }
                    }
                }
            });
        } else {
            console.error('Invalid data format:', data);
            $('#ratio-chart').parent().html('<p>비율 데이터 형식이 올바르지 않습니다.</p>');
        }
    }).fail(function(jqXHR, textStatus, errorThrown) {
        console.error('Error fetching ratio data:', textStatus, errorThrown);
        $('#ratio-chart').parent().html('<p>비율 데이터를 불러오는 중 오류가 발생했습니다.</p>');
    });
}

// DOM이 로드된 후 함수 호출
$(document).ready(function() {
    console.log('Document ready, calling fetchRatioData');
    fetchRatioData();
});

            // 결제 그래프
            function fetchAndUpdateData() {
                $.get('/api/all-job-posts', function(data) {
                    console.log('API Response:', data);

                    let totalExpenses = 0;

                    data.forEach(map => {
                        console.log('Map:', map);

                        const amount = parseFloat(map.TOTALAMOUNT);
                        if (!isNaN(amount)) {
                            console.log('Amount:', amount);
                            totalExpenses += amount;
                        }
                    });

                    $('#totalExpenses').text(totalExpenses);

                    const maxValue = 100000;
                    const percentage = Math.min((totalExpenses / maxValue) * 100, 100);

                    $('#progressBar')
                        .css('width', percentage + '%')
                        .attr('aria-valuenow', percentage);
                }).fail(function(jqXHR, textStatus, errorThrown) {
                    console.error('Error fetching data:', textStatus, errorThrown);
                    $('#totalExpenses').text('Error');
                    $('#progressBar').css('width', '0%').attr('aria-valuenow', '0');
                });
            }

            // 공고 조회수 그래프
            function fetchTotalViews() {
                $.get('/api/total-job-post-views', function(data) {
                    $('#totalViews').text(data);
                    console.log('totalViews:', data);

                    const maxViews = 50;
                    const percentage = Math.min((data / maxViews) * 100, 100);

                    $('#viewProgressBar')
                        .css('width', percentage + '%')
                        .attr('aria-valuenow', percentage);
                }).fail(function(jqXHR, textStatus, errorThrown) {
                    console.error('Error fetching total views:', textStatus, errorThrown);
                    $('#totalViews').text('Error');
                    $('#viewProgressBar').css('width', '0%').attr('aria-valuenow', '0');
                });
            }

            // 총 공고 수 그래프
            function fetchTotalJobPosts() {
                $.get('/api/total-job-posts', function(data) {
                    $('#totalJobPosts').text(data);
                    console.log('Total Job Posts:', data);

                    const maxValue = 20;
                    const percentage = Math.min((data / maxValue) * 100, 100);

                    $('#jobPostProgressBar')
                        .css('width', percentage + '%')
                        .attr('aria-valuenow', percentage);
                }).fail(function(jqXHR, textStatus, errorThrown) {
                    console.error('Error fetching total job posts:', textStatus, errorThrown);
                    $('#totalJobPosts').text('Error');
                    $('#jobPostProgressBar').css('width', '0%').attr('aria-valuenow', '0');
                });
            }

            // 총 지원서 수 그래프
            function fetchTotalApplications() {
                $.get('/api/total-applications', function(data) {
                    $('#totalApplications').text(data);
                    console.log('Total Applications:', data);

                    const maxValue = 20;
                    const percentage = Math.min((data / maxValue) * 100, 100);

                    $('#applicationsProgressBar')
                        .css('width', percentage + '%')
                        .attr('aria-valuenow', percentage);
                }).fail(function(jqXHR, textStatus, errorThrown) {
                    console.error('Error fetching total applications:', textStatus, errorThrown);
                    $('#totalApplications').text('Error');
                    $('#applicationsProgressBar').css('width', '0%').attr('aria-valuenow', '0');
                });
            }

   function fetchMonthlyRevenue(year) {
       $.get('/api/monthly-overview', { year: year }, function (data) {
           console.log('Raw Monthly Revenue Data:', data);

           $('#morris-line-chart').empty();

           // 데이터 유효성 검사 및 전처리
           var validData = data.filter(function(item) {
               return item && item.month && item.totalRevenue !== undefined;
           }).map(function(item) {
               return {
                   month: item.month,
                   totalRevenue: parseFloat(item.totalRevenue) || 0
               };
           });

           console.log('Processed Data:', validData);

           if (validData.length === 0) {
               console.error('No valid data to display');
               $('#morris-line-chart').html('<p>데이터가 없습니다.</p>');
               return;
           }

           try {
               Morris.Line({
                   element: 'morris-line-chart',
                   data: validData,
                   xkey: 'month',
                   ykeys: ['totalRevenue'],
                   labels: ['Total Revenue'],
                   lineColors: ['#1ab394'],
                   xLabelFormat: function (x) {
                       console.log('xLabelFormat input:', x);
                       const monthNames = ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"];
                       // x.label에서 월 정보를 추출합니다.
                       const monthIndex = parseInt(x.label, 10) - 1;
                       return monthNames[monthIndex] || x.label;
                   },
                   yLabelFormat: function(y) {
                       console.log('yLabelFormat input:', y);
                       return y.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + '원';
                   },
                   xLabels: "month",
                   hideHover: 'auto',
                   resize: true,
                   parseTime: false,
                   xLabelAngle: 45
               });
               console.log('Chart created successfully');
           } catch (error) {
               console.error('Error creating chart:', error);
               $('#morris-line-chart').html('<p>차트 생성 중 오류가 발생했습니다.</p>');
           }

       }).fail(function (jqXHR, textStatus, errorThrown) {
           console.error('Error fetching monthly revenue:', textStatus, errorThrown);
           $('#morris-line-chart').html('<p>데이터를 불러오는 중 오류가 발생했습니다.</p>');
       });
   }

   // DOM이 로드된 후 함수 호출
   $(document).ready(function() {
       console.log('Document ready, calling fetchMonthlyRevenue');
       fetchMonthlyRevenue(new Date().getFullYear());
   });

            // 데이터 로드 및 갱신 설정
            fetchAndUpdateData();
            fetchTotalViews();
            fetchTotalJobPosts();
            fetchTotalApplications();
            fetchMonthlyRevenue(2024);

            setInterval(fetchAndUpdateData, 30000);
            setInterval(fetchTotalViews, 30000);
            setInterval(fetchTotalJobPosts, 30000);
            setInterval(fetchTotalApplications, 30000);
            setInterval(() => fetchMonthlyRevenue(2024), 30000);
        });