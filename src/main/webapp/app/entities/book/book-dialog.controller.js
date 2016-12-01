(function() {
    'use strict';

    angular
        .module('libraryApp')
        .controller('BookDialogController', BookDialogController);

    BookDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Book'];

    function BookDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Book) {
        var vm = this;

        vm.book = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.book.id !== null) {
                Book.update(vm.book, onSaveSuccess, onSaveError);
            } else {
                Book.save(vm.book, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('libraryApp:bookUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.issuedDate = false;
        vm.datePickerOpenStatus.returningDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
