(function() {
    'use strict';
    angular
        .module('libraryApp')
        .factory('Book', Book);

    Book.$inject = ['$resource', 'DateUtils'];

    function Book ($resource, DateUtils) {
        var resourceUrl =  'api/books/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.issuedDate = DateUtils.convertLocalDateFromServer(data.issuedDate);
                        data.returningDate = DateUtils.convertLocalDateFromServer(data.returningDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.issuedDate = DateUtils.convertLocalDateToServer(copy.issuedDate);
                    copy.returningDate = DateUtils.convertLocalDateToServer(copy.returningDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.issuedDate = DateUtils.convertLocalDateToServer(copy.issuedDate);
                    copy.returningDate = DateUtils.convertLocalDateToServer(copy.returningDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
