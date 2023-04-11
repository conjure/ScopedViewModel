<#import "source_set_selector.ftl" as source_set_selector>
<#macro display>
<div class="navigation-wrapper" id="navigation-wrapper">
    <div id="leftToggler"><span class="icon-toggler"></span></div>
    <div style="font-size: 1.875rem; line-height: 2.25rem; margin-right: 1em;">
        <a href="/android">
            Conjure <span style="color: #767779;">Developer</span>
        </a>
    </div>
    <div class="xlibrary-name">
        <@template_cmd name="pathToRoot">
            <a href="${pathToRoot}index.html">
                <@template_cmd name="projectName">
                    <span>${projectName}</span>
                </@template_cmd>
            </a>
        </@template_cmd>
    </div>
    <div>
        <#-- This can be handled by the versioning plugin -->
        <@version/>
    </div>
    <div class="pull-right d-flex">
           <button id="theme-toggle-button"><span id="theme-toggle"></span></button>
           <div id="searchBar"></div>
       </div>
</div>
</#macro>