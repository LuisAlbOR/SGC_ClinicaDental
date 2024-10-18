<template>
  <!-- source:https://codepen.io/owaiswiz/pen/jOPvEPB -->
  <div class="min-h-screen bg-gray-100 text-gray-900 flex justify-center">
    <div class="max-w-screen-xl m-0 sm:m-5 bg-white shadow sm:rounded-lg flex justify-center flex-1">
      <RouterView />
      <div class="flex-1 bg-blue-200 text-center hidden lg:flex">
        <div class="m-12 xl:m-1.5 w-full flex justify-center">
          <div class="w-1/2 flex justify-center items-center bg-contain bg-center bg-no-repeat">
            <img src="../../../../public/undraw_medicine_b-1-ol.svg" class="w-full h-auto" />
          </div>

        </div>
      </div>
    </div>
  </div>
</template>