    // 결제 그래프
    document.addEventListener('DOMContentLoaded', function () {
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

        fetchAndUpdateData();
        setInterval(fetchAndUpdateData, 30000);
    });
     // 공고 조회수 그래프
    document.addEventListener('DOMContentLoaded', function () {
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

        fetchTotalViews();
        setInterval(fetchTotalViews, 30000);
    });
        // 총 공고 곗수 그래프
       document.addEventListener('DOMContentLoaded', function () {
            // 총 공고 수를 가져와서 업데이트하는 함수
            function fetchTotalJobPosts() {
                $.get('/api/total-job-posts', function(data) {
                    $('#totalJobPosts').text(data); // 총 공고 수 표시
                    console.log('Total Job Posts:', data); // 콘솔에 출력하여 확인

                    // 진행 바 업데이트 (목표는 1000개로 설정, 필요에 따라 조정 가능)
                    const maxValue = 20; // 목표 공고 수 설정
                    const percentage = Math.min((data / maxValue) * 100, 100);

                    $('#jobPostProgressBar')
                        .css('width', percentage + '%')
                        .attr('aria-valuenow', percentage);
                }).fail(function(jqXHR, textStatus, errorThrown) {
                    console.error('Error fetching total job posts:', textStatus, errorThrown);
                    $('#totalJobPosts').text('Error'); // 오류 발생 시 사용자에게 표시
                    $('#jobPostProgressBar').css('width', '0%').attr('aria-valuenow', '0');
                });
            }

            // 페이지 로드 시 총 공고 수 로드
            fetchTotalJobPosts();

            // 데이터 갱신을 위한 주기 설정 (예: 30초마다 업데이트)
            setInterval(fetchTotalJobPosts, 30000);
        });

         document.addEventListener('DOMContentLoaded', function () {
                // 총 지원서 수를 가져와서 업데이트하는 함수
                function fetchTotalApplications() {
                    $.get('/api/total-applications', function(data) {
                        $('#totalApplications').text(data); // 총 지원서 수 표시
                        console.log('Total Applications:', data); // 콘솔에 출력하여 확인

                        // 진행 바 업데이트 (목표는 500개로 설정, 필요에 따라 조정 가능)
                        const maxValue = 20; // 목표 지원서 수 설정
                        const percentage = Math.min((data / maxValue) * 100, 100);

                        $('#applicationsProgressBar')
                            .css('width', percentage + '%')
                            .attr('aria-valuenow', percentage);
                    }).fail(function(jqXHR, textStatus, errorThrown) {
                        console.error('Error fetching total applications:', textStatus, errorThrown);
                        $('#totalApplications').text('Error'); // 오류 발생 시 사용자에게 표시
                        $('#applicationsProgressBar').css('width', '0%').attr('aria-valuenow', '0');
                    });
                }

                // 페이지 로드 시 총 지원서 수 로드
                fetchTotalApplications();

                // 데이터 갱신을 위한 주기 설정 (예: 30초마다 업데이트)
                setInterval(fetchTotalApplications, 30000);
            });


               document.addEventListener('DOMContentLoaded', function () {
                    function fetchYearlyOverview() {
                        $.get('/api/yearly-overview', function(data) {
                            console.log('Yearly Overview Data:', data);

                            // Morris.js 바 차트에 데이터 로드
                            Morris.Bar({
                                element: 'morris-bar-chart',
                                data: data,
                                xkey: 'year',
                                ykeys: ['totalRevenue', 'totalApplications', 'totalJobPosts'],
                                labels: ['Total Revenue', 'Total Applications', 'Total Job Posts'],
                                barColors: ['#1ab394', '#1c84c6', '#ed5565'],
                                hideHover: 'auto',
                                resize: true
                            });

                        }).fail(function(jqXHR, textStatus, errorThrown) {
                            console.error('Error fetching yearly overview:', textStatus, errorThrown);
                        });
                    }

                    // 페이지 로드 시 연도별 개요 데이터 로드
                    fetchYearlyOverview();

                    // 데이터 갱신을 위한 주기 설정 (예: 30초마다 업데이트)
                    setInterval(fetchYearlyOverview, 30000);
                });